public void GenerateInitMoves(java.io.PrintWriter ostr) {
    GetEpsilonMovesString();
    if (epsilonMovesString == null)
        epsilonMovesString = "null;";
    AddStartStateSet(epsilonMovesString);
}
