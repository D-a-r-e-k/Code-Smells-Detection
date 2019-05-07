final boolean CanMoveUsingChar(char c) {
    int i;
    if (onlyChar == 1)
        return c == matchSingleChar;
    if (c < 128)
        return ((asciiMoves[c / 64] & (1L << c % 64)) != 0L);
    // Just check directly if there is a move for this char 
    if (charMoves != null && charMoves[0] != 0) {
        for (i = 0; i < charMoves.length; i++) {
            if (c == charMoves[i])
                return true;
            else if (c < charMoves[i] || charMoves[i] == 0)
                break;
        }
    }
    // For ranges, iterate thru the table to see if the current char 
    // is in some range 
    if (rangeMoves != null && rangeMoves[0] != 0)
        for (i = 0; i < rangeMoves.length; i += 2) if (c >= rangeMoves[i] && c <= rangeMoves[i + 1])
            return true;
        else if (c < rangeMoves[i] || rangeMoves[i] == 0)
            break;
    //return (nextForNegatedList != null); 
    return false;
}
