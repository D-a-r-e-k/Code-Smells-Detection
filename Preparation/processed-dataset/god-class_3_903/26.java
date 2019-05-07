public static int moveFromSetForRegEx(char c, NfaState[] states, NfaState[] newStates, int round) {
    int start = 0;
    int sz = states.length;
    for (int i = 0; i < sz; i++) {
        NfaState tmp1, tmp2;
        if ((tmp1 = states[i]) == null)
            break;
        if (tmp1.CanMoveUsingChar(c)) {
            if (tmp1.kindToPrint != Integer.MAX_VALUE) {
                newStates[start] = null;
                return 1;
            }
            NfaState[] v = tmp1.next.epsilonMoveArray;
            for (int j = v.length; j-- > 0; ) {
                if ((tmp2 = v[j]).round != round) {
                    tmp2.round = round;
                    newStates[start++] = tmp2;
                }
            }
        }
    }
    newStates[start] = null;
    return Integer.MAX_VALUE;
}
