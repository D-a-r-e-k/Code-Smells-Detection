public boolean HasTransitions() {
    return (asciiMoves[0] != 0L || asciiMoves[1] != 0L || (charMoves != null && charMoves[0] != 0) || (rangeMoves != null && rangeMoves[0] != 0));
}
