/**
     * Returns whether any of the given instruction offsets (at least one)
     * is larger than the given offset.
     */
private boolean isAnyLargerThan(InstructionOffsetValue instructionOffsets, int instructionOffset) {
    if (instructionOffsets != null) {
        // Loop over all instruction offsets. 
        int branchCount = instructionOffsets.instructionOffsetCount();
        if (branchCount > 0) {
            for (int branchIndex = 0; branchIndex < branchCount; branchIndex++) {
                // Is the offset larger than the reference offset? 
                if (instructionOffsets.instructionOffset(branchIndex) > instructionOffset) {
                    return true;
                }
            }
        }
    }
    return false;
}
