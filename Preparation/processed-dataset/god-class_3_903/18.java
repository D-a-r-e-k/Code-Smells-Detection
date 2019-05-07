void OptimizeEpsilonMoves(boolean optReqd) {
    int i;
    // First do epsilon closure 
    done = false;
    while (!done) {
        if (mark == null || mark.length < allStates.size())
            mark = new boolean[allStates.size()];
        for (i = allStates.size(); i-- > 0; ) mark[i] = false;
        done = true;
        EpsilonClosure();
    }
    for (i = allStates.size(); i-- > 0; ) ((NfaState) allStates.get(i)).closureDone = mark[((NfaState) allStates.get(i)).id];
    // Warning : The following piece of code is just an optimization. 
    // in case of trouble, just remove this piece. 
    boolean sometingOptimized = true;
    NfaState newState = null;
    NfaState tmp1, tmp2;
    int j;
    List equivStates = null;
    while (sometingOptimized) {
        sometingOptimized = false;
        for (i = 0; optReqd && i < epsilonMoves.size(); i++) {
            if ((tmp1 = (NfaState) epsilonMoves.get(i)).HasTransitions()) {
                for (j = i + 1; j < epsilonMoves.size(); j++) {
                    if ((tmp2 = (NfaState) epsilonMoves.get(j)).HasTransitions() && (tmp1.asciiMoves[0] == tmp2.asciiMoves[0] && tmp1.asciiMoves[1] == tmp2.asciiMoves[1] && EqualCharArr(tmp1.charMoves, tmp2.charMoves) && EqualCharArr(tmp1.rangeMoves, tmp2.rangeMoves))) {
                        if (equivStates == null) {
                            equivStates = new ArrayList();
                            equivStates.add(tmp1);
                        }
                        InsertInOrder(equivStates, tmp2);
                        epsilonMoves.removeElementAt(j--);
                    }
                }
            }
            if (equivStates != null) {
                sometingOptimized = true;
                String tmp = "";
                for (int l = 0; l < equivStates.size(); l++) tmp += String.valueOf(((NfaState) equivStates.get(l)).id) + ", ";
                if ((newState = (NfaState) equivStatesTable.get(tmp)) == null) {
                    newState = CreateEquivState(equivStates);
                    equivStatesTable.put(tmp, newState);
                }
                epsilonMoves.removeElementAt(i--);
                epsilonMoves.add(newState);
                equivStates = null;
                newState = null;
            }
        }
        for (i = 0; i < epsilonMoves.size(); i++) {
            //if ((tmp1 = (NfaState)epsilonMoves.elementAt(i)).next == null) 
            //continue; 
            tmp1 = (NfaState) epsilonMoves.get(i);
            for (j = i + 1; j < epsilonMoves.size(); j++) {
                tmp2 = (NfaState) epsilonMoves.get(j);
                if (tmp1.next == tmp2.next) {
                    if (newState == null) {
                        newState = tmp1.CreateClone();
                        newState.next = tmp1.next;
                        sometingOptimized = true;
                    }
                    newState.MergeMoves(tmp2);
                    epsilonMoves.removeElementAt(j--);
                }
            }
            if (newState != null) {
                epsilonMoves.removeElementAt(i--);
                epsilonMoves.add(newState);
                newState = null;
            }
        }
    }
    // End Warning 
    // Generate an array of states for epsilon moves (not vector) 
    if (epsilonMoves.size() > 0) {
        for (i = 0; i < epsilonMoves.size(); i++) // Since we are doing a closure, just epsilon moves are unncessary 
        if (((NfaState) epsilonMoves.get(i)).HasTransitions())
            usefulEpsilonMoves++;
        else
            epsilonMoves.removeElementAt(i--);
    }
}
