void MergeMoves(NfaState other) {
    // Warning : This function does not merge epsilon moves 
    if (asciiMoves == other.asciiMoves) {
        JavaCCErrors.semantic_error("Bug in JavaCC : Please send " + "a report along with the input that caused this. Thank you.");
        throw new Error();
    }
    asciiMoves[0] = asciiMoves[0] | other.asciiMoves[0];
    asciiMoves[1] = asciiMoves[1] | other.asciiMoves[1];
    if (other.charMoves != null) {
        if (charMoves == null)
            charMoves = other.charMoves;
        else {
            char[] tmpCharMoves = new char[charMoves.length + other.charMoves.length];
            System.arraycopy(charMoves, 0, tmpCharMoves, 0, charMoves.length);
            charMoves = tmpCharMoves;
            for (int i = 0; i < other.charMoves.length; i++) AddChar(other.charMoves[i]);
        }
    }
    if (other.rangeMoves != null) {
        if (rangeMoves == null)
            rangeMoves = other.rangeMoves;
        else {
            char[] tmpRangeMoves = new char[rangeMoves.length + other.rangeMoves.length];
            System.arraycopy(rangeMoves, 0, tmpRangeMoves, 0, rangeMoves.length);
            rangeMoves = tmpRangeMoves;
            for (int i = 0; i < other.rangeMoves.length; i += 2) AddRange(other.rangeMoves[i], other.rangeMoves[i + 1]);
        }
    }
    if (other.kind < kind)
        kind = other.kind;
    if (other.kindToPrint < kindToPrint)
        kindToPrint = other.kindToPrint;
    isFinal |= other.isFinal;
}
