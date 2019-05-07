void GenerateNextStatesCode() {
    if (next.usefulEpsilonMoves > 0)
        next.GetEpsilonMovesString();
}
