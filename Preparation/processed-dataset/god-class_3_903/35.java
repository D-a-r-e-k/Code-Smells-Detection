static int InitStateName() {
    String s = LexGen.initialState.GetEpsilonMovesString();
    if (LexGen.initialState.usefulEpsilonMoves != 0)
        return StateNameForComposite(s);
    return -1;
}
