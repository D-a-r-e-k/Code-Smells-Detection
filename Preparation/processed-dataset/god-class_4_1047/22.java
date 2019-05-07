/**
     * Returns whether all of the given instruction offsets (at least one)
     * are smaller than or equal to the given offset.
     */
private boolean isAllSmallerThanOrEqual(InstructionOffsetValue instructionOffsets, int instructionOffset) {
    if (instructionOffsets != null) {
        // Loop over all instruction offsets. 
        int branchCount = instructionOffsets.instructionOffsetCount();
        if (branchCount > 0) {
            for (int branchIndex = 0; branchIndex < branchCount; branchIndex++) {
                // Is the offset larger than the reference offset? 
                if (instructionOffsets.instructionOffset(branchIndex) > instructionOffset) {
                    return false;
                }
            }
            return true;
        }
    }
    return false;
}
