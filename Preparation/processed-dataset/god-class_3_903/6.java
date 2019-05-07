private final void AddASCIIMove(char c) {
    asciiMoves[c / 64] |= (1L << (c % 64));
}
