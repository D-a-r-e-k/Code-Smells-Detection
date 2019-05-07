/**
     * Returns whether there is an instruction at the given offset.
     */
public boolean isInstruction(int instructionOffset) {
    return branchTargetFinder.isInstruction(instructionOffset);
}
