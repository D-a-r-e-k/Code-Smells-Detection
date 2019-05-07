public static void ComputeClosures() {
    for (int i = allStates.size(); i-- > 0; ) {
        NfaState tmp = (NfaState) allStates.get(i);
        if (!tmp.closureDone)
            tmp.OptimizeEpsilonMoves(true);
    }
    for (int i = 0; i < allStates.size(); i++) {
        NfaState tmp = (NfaState) allStates.get(i);
        if (!tmp.closureDone)
            tmp.OptimizeEpsilonMoves(false);
    }
    for (int i = 0; i < allStates.size(); i++) {
        NfaState tmp = (NfaState) allStates.get(i);
        tmp.epsilonMoveArray = new NfaState[tmp.epsilonMoves.size()];
        tmp.epsilonMoves.copyInto(tmp.epsilonMoveArray);
    }
}
