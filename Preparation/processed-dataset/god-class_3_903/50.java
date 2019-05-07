private static Vector PartitionStatesSetForAscii(int[] states, int byteNum) {
    int[] cardinalities = new int[states.length];
    Vector original = new Vector();
    Vector partition = new Vector();
    NfaState tmp;
    original.setSize(states.length);
    int cnt = 0;
    for (int i = 0; i < states.length; i++) {
        tmp = (NfaState) allStates.get(states[i]);
        if (tmp.asciiMoves[byteNum] != 0L) {
            int j;
            int p = NumberOfBitsSet(tmp.asciiMoves[byteNum]);
            for (j = 0; j < i; j++) if (cardinalities[j] <= p)
                break;
            for (int k = i; k > j; k--) cardinalities[k] = cardinalities[k - 1];
            cardinalities[j] = p;
            original.insertElementAt(tmp, j);
            cnt++;
        }
    }
    original.setSize(cnt);
    while (original.size() > 0) {
        tmp = (NfaState) original.get(0);
        original.removeElement(tmp);
        long bitVec = tmp.asciiMoves[byteNum];
        List subSet = new ArrayList();
        subSet.add(tmp);
        for (int j = 0; j < original.size(); j++) {
            NfaState tmp1 = (NfaState) original.get(j);
            if ((tmp1.asciiMoves[byteNum] & bitVec) == 0L) {
                bitVec |= tmp1.asciiMoves[byteNum];
                subSet.add(tmp1);
                original.removeElementAt(j--);
            }
        }
        partition.add(subSet);
    }
    return partition;
}
