public static boolean CanStartNfaUsingAscii(char c) {
    if (c >= 128)
        throw new Error("JavaCC Bug: Please send mail to sankar@cs.stanford.edu");
    String s = LexGen.initialState.GetEpsilonMovesString();
    if (s == null || s.equals("null;"))
        return false;
    int[] states = (int[]) allNextStates.get(s);
    for (int i = 0; i < states.length; i++) {
        NfaState tmp = (NfaState) indexedAllStates.get(states[i]);
        if ((tmp.asciiMoves[c / 64] & (1L << c % 64)) != 0L)
            return true;
    }
    return false;
}
