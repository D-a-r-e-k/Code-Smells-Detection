/**
     * Returns whether the instruction at the given offset has ever been
     * executed during the partial evaluation.
     */
public boolean isTraced(int instructionOffset) {
    return evaluationCounts[instructionOffset] > 0;
}
