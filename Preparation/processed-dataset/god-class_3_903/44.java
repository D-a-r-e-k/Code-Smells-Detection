private boolean FindCommonBlocks() {
    if (next == null || next.usefulEpsilonMoves <= 1)
        return false;
    if (stateDone == null)
        stateDone = new boolean[generatedStates];
    String set = next.epsilonMovesString;
    int[] nameSet = (int[]) allNextStates.get(set);
    if (nameSet.length <= 2 || compositeStateTable.get(set) != null)
        return false;
    int i;
    int freq[] = new int[nameSet.length];
    boolean live[] = new boolean[nameSet.length];
    int[] count = new int[allNextStates.size()];
    for (i = 0; i < nameSet.length; i++) {
        if (nameSet[i] != -1) {
            if (live[i] = !stateDone[nameSet[i]])
                count[0]++;
        }
    }
    int j, blockLen = 0, commonFreq = 0;
    Enumeration e = allNextStates.keys();
    boolean needUpdate;
    while (e.hasMoreElements()) {
        int[] tmpSet = (int[]) allNextStates.get(e.nextElement());
        if (tmpSet == nameSet)
            continue;
        needUpdate = false;
        for (j = 0; j < nameSet.length; j++) {
            if (nameSet[j] == -1)
                continue;
            if (live[j] && ElemOccurs(nameSet[j], tmpSet) >= 0) {
                if (!needUpdate) {
                    needUpdate = true;
                    commonFreq++;
                }
                count[freq[j]]--;
                count[commonFreq]++;
                freq[j] = commonFreq;
            }
        }
        if (needUpdate) {
            int foundFreq = -1;
            blockLen = 0;
            for (j = 0; j <= commonFreq; j++) if (count[j] > blockLen) {
                foundFreq = j;
                blockLen = count[j];
            }
            if (blockLen <= 1)
                return false;
            for (j = 0; j < nameSet.length; j++) if (nameSet[j] != -1 && freq[j] != foundFreq) {
                live[j] = false;
                count[freq[j]]--;
            }
        }
    }
    if (blockLen <= 1)
        return false;
    int[] commonBlock = new int[blockLen];
    int cnt = 0;
    //System.out.println("Common Block for " + set + " :"); 
    for (i = 0; i < nameSet.length; i++) {
        if (live[i]) {
            if (((NfaState) indexedAllStates.get(nameSet[i])).isComposite)
                return false;
            stateDone[nameSet[i]] = true;
            commonBlock[cnt++] = nameSet[i];
        }
    }
    //System.out.println(""); 
    String s = GetStateSetString(commonBlock);
    e = allNextStates.keys();
    Outer: while (e.hasMoreElements()) {
        int at;
        boolean firstOne = true;
        String stringToFix;
        int[] setToFix = (int[]) allNextStates.get(stringToFix = (String) e.nextElement());
        if (setToFix == commonBlock)
            continue;
        for (int k = 0; k < cnt; k++) {
            if ((at = ElemOccurs(commonBlock[k], setToFix)) >= 0) {
                if (!firstOne)
                    setToFix[at] = -1;
                firstOne = false;
            } else
                continue Outer;
        }
        if (stateSetsToFix.get(stringToFix) == null)
            stateSetsToFix.put(stringToFix, setToFix);
    }
    next.usefulEpsilonMoves -= blockLen - 1;
    AddCompositeStateSet(s, false);
    return true;
}
