private NfaState GetEquivalentRunTimeState() {
    Outer: for (int i = allStates.size(); i-- > 0; ) {
        NfaState other = (NfaState) allStates.get(i);
        if (this != other && other.stateName != -1 && kindToPrint == other.kindToPrint && asciiMoves[0] == other.asciiMoves[0] && asciiMoves[1] == other.asciiMoves[1] && EqualCharArr(charMoves, other.charMoves) && EqualCharArr(rangeMoves, other.rangeMoves)) {
            if (next == other.next)
                return other;
            else if (next != null && other.next != null) {
                if (next.epsilonMoves.size() == other.next.epsilonMoves.size()) {
                    for (int j = 0; j < next.epsilonMoves.size(); j++) if (next.epsilonMoves.get(j) != other.next.epsilonMoves.get(j))
                        continue Outer;
                    return other;
                }
            }
        }
    }
    return null;
}
