private static void FindStatesWithNoBreak() {
    Hashtable printed = new Hashtable();
    boolean[] put = new boolean[generatedStates];
    int cnt = 0;
    int i, j, foundAt = 0;
    Outer: for (j = 0; j < allStates.size(); j++) {
        NfaState stateForCase = null;
        NfaState tmpState = (NfaState) allStates.get(j);
        if (tmpState.stateName == -1 || tmpState.dummy || !tmpState.UsefulState() || tmpState.next == null || tmpState.next.usefulEpsilonMoves < 1)
            continue;
        String s = tmpState.next.epsilonMovesString;
        if (compositeStateTable.get(s) != null || printed.get(s) != null)
            continue;
        printed.put(s, s);
        int[] nexts = (int[]) allNextStates.get(s);
        if (nexts.length == 1)
            continue;
        int state = cnt;
        //System.out.println("State " + tmpState.stateName + " : " + s); 
        for (i = 0; i < nexts.length; i++) {
            if ((state = nexts[i]) == -1)
                continue;
            NfaState tmp = (NfaState) allStates.get(state);
            if (!tmp.isComposite && tmp.inNextOf == 1) {
                if (put[state])
                    throw new Error("JavaCC Bug: Please send mail to sankar@cs.stanford.edu");
                foundAt = i;
                cnt++;
                stateForCase = tmp;
                put[state] = true;
                //System.out.print(state + " : " + tmp.inNextOf + ", "); 
                break;
            }
        }
        //System.out.println(""); 
        if (stateForCase == null)
            continue;
        for (i = 0; i < nexts.length; i++) {
            if ((state = nexts[i]) == -1)
                continue;
            NfaState tmp = (NfaState) allStates.get(state);
            if (!put[state] && tmp.inNextOf > 1 && !tmp.isComposite && tmp.stateForCase == null) {
                cnt++;
                nexts[i] = -1;
                put[state] = true;
                int toSwap = nexts[0];
                nexts[0] = nexts[foundAt];
                nexts[foundAt] = toSwap;
                tmp.stateForCase = stateForCase;
                stateForCase.stateForCase = tmp;
                stateSetsToFix.put(s, nexts);
                //System.out.println("For : " + s + "; " + stateForCase.stateName + 
                //" and " + tmp.stateName); 
                continue Outer;
            }
        }
        for (i = 0; i < nexts.length; i++) {
            if ((state = nexts[i]) == -1)
                continue;
            NfaState tmp = (NfaState) allStates.get(state);
            if (tmp.inNextOf <= 1)
                put[state] = false;
        }
    }
}
