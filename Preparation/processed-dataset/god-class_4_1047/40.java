/**
     * Returns the highest offset of an instruction that has been marked as
     * necessary, before the given offset.
     */
private int lastNecessaryInstructionOffset(int instructionOffset) {
    for (int offset = instructionOffset - 1; offset >= 0; offset--) {
        if (isInstructionNecessary(instructionOffset)) {
            return offset;
        }
    }
    return 0;
}
