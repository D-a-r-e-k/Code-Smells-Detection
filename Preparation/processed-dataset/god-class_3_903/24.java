public int MoveFrom(char c, List newStates) {
    if (CanMoveUsingChar(c)) {
        for (int i = next.epsilonMoves.size(); i-- > 0; ) InsertInOrder(newStates, (NfaState) next.epsilonMoves.get(i));
        return kindToPrint;
    }
    return Integer.MAX_VALUE;
}
