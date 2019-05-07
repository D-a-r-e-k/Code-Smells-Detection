public int[] occurenceInfo(int[] state) {
    if (fCountingStates != null) {
        int curState = state[0];
        if (curState < 0) {
            curState = state[1];
        }
        Occurence o = fCountingStates[curState];
        if (o != null) {
            int[] occurenceInfo = new int[4];
            occurenceInfo[0] = o.minOccurs;
            occurenceInfo[1] = o.maxOccurs;
            occurenceInfo[2] = state[2];
            occurenceInfo[3] = o.elemIndex;
            return occurenceInfo;
        }
    }
    return null;
}
