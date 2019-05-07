private boolean CheckNextOccursTogether() {
    if (next == null || next.usefulEpsilonMoves <= 1)
        return true;
    String set = next.epsilonMovesString;
    int[] nameSet = (int[]) allNextStates.get(set);
    if (nameSet.length == 1 || compositeStateTable.get(set) != null || stateSetsToFix.get(set) != null)
        return false;
    int i;
    Hashtable occursIn = new Hashtable();
    NfaState tmp = (NfaState) allStates.get(nameSet[0]);
    for (i = 1; i < nameSet.length; i++) {
        NfaState tmp1 = (NfaState) allStates.get(nameSet[i]);
        if (tmp.inNextOf != tmp1.inNextOf)
            return false;
    }
    int isPresent, j;
    Enumeration e = allNextStates.keys();
    while (e.hasMoreElements()) {
        String s;
        int[] tmpSet = (int[]) allNextStates.get(s = (String) e.nextElement());
        if (tmpSet == nameSet)
            continue;
        isPresent = 0;
        for (j = 0; j < nameSet.length; j++) {
            if (ElemOccurs(nameSet[j], tmpSet) >= 0)
                isPresent++;
            else if (isPresent > 0)
                return false;
        }
        if (isPresent == j) {
            if (tmpSet.length > nameSet.length)
                occursIn.put(s, tmpSet);
            //May not need. But safe. 
            if (compositeStateTable.get(s) != null || stateSetsToFix.get(s) != null)
                return false;
        } else if (isPresent != 0)
            return false;
    }
    e = occursIn.keys();
    while (e.hasMoreElements()) {
        String s;
        int[] setToFix = (int[]) occursIn.get(s = (String) e.nextElement());
        if (stateSetsToFix.get(s) == null)
            stateSetsToFix.put(s, setToFix);
        for (int k = 0; k < setToFix.length; k++) if (ElemOccurs(setToFix[k], nameSet) > 0)
            // Not >= since need the first one (0) 
            setToFix[k] = -1;
    }
    next.usefulEpsilonMoves = 1;
    AddCompositeStateSet(next.epsilonMovesString, false);
    return true;
}
