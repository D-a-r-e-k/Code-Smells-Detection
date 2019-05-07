/**
     * Returns whether the instruction at the given offset is the target of a
     * branch instruction or an exception.
     */
public boolean isBranchOrExceptionTarget(int instructionOffset) {
    return branchTargetFinder.isBranchTarget(instructionOffset) || branchTargetFinder.isExceptionHandler(instructionOffset);
}
