/**
     * Returns whether the instruction at the given offset is the start of a
     * subroutine.
     */
public boolean isSubroutineStart(int instructionOffset) {
    return branchTargetFinder.isSubroutineStart(instructionOffset);
}
