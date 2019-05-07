void AddMove(NfaState newState) {
    if (!epsilonMoves.contains(newState))
        InsertInOrder(epsilonMoves, newState);
}
