/**
     * Marks the branch instructions of straddling branches, if they straddle
     * some code that has been marked.
     * @param instructionOffset   the offset of the branch origin or branch target.
     * @param branchOffsets       the offsets of the straddling branch targets
     *                            or branch origins.
     * @param isPointingToTargets <code>true</code> if the above offsets are
     *                            branch targets, <code>false</code> if they
     *                            are branch origins.
     */
private void markStraddlingBranches(int instructionOffset, InstructionOffsetValue branchOffsets, boolean isPointingToTargets) {
    if (branchOffsets != null) {
        // Loop over all branch offsets. 
        int branchCount = branchOffsets.instructionOffsetCount();
        for (int branchIndex = 0; branchIndex < branchCount; branchIndex++) {
            // Is the branch straddling forward any necessary instructions? 
            int branchOffset = branchOffsets.instructionOffset(branchIndex);
            // Is the offset pointing to a branch origin or to a branch target? 
            if (isPointingToTargets) {
                markStraddlingBranch(instructionOffset, branchOffset, instructionOffset, branchOffset);
            } else {
                markStraddlingBranch(instructionOffset, branchOffset, branchOffset, instructionOffset);
            }
        }
    }
}
