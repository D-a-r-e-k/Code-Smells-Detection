private void UpdateDuplicateNonAsciiMoves() {
    for (int i = 0; i < nonAsciiTableForMethod.size(); i++) {
        NfaState tmp = (NfaState) nonAsciiTableForMethod.get(i);
        if (EqualLoByteVectors(loByteVec, tmp.loByteVec) && EqualNonAsciiMoveIndices(nonAsciiMoveIndices, tmp.nonAsciiMoveIndices)) {
            nonAsciiMethod = i;
            return;
        }
    }
    nonAsciiMethod = nonAsciiTableForMethod.size();
    nonAsciiTableForMethod.add(this);
}
