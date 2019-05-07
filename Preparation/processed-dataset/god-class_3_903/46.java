private static void FixStateSets() {
    Hashtable fixedSets = new Hashtable();
    Enumeration e = stateSetsToFix.keys();
    int[] tmp = new int[generatedStates];
    int i;
    while (e.hasMoreElements()) {
        String s;
        int[] toFix = (int[]) stateSetsToFix.get(s = (String) e.nextElement());
        int cnt = 0;
        //System.out.print("Fixing : "); 
        for (i = 0; i < toFix.length; i++) {
            //System.out.print(toFix[i] + ", "); 
            if (toFix[i] != -1)
                tmp[cnt++] = toFix[i];
        }
        int[] fixed = new int[cnt];
        System.arraycopy(tmp, 0, fixed, 0, cnt);
        fixedSets.put(s, fixed);
        allNextStates.put(s, fixed);
    }
    for (i = 0; i < allStates.size(); i++) {
        NfaState tmpState = (NfaState) allStates.get(i);
        int[] newSet;
        if (tmpState.next == null || tmpState.next.usefulEpsilonMoves == 0)
            continue;
        /*if (compositeStateTable.get(tmpState.next.epsilonMovesString) != null)
            tmpState.next.usefulEpsilonMoves = 1;
         else*/
        if ((newSet = (int[]) fixedSets.get(tmpState.next.epsilonMovesString)) != null)
            tmpState.FixNextStates(newSet);
    }
}
