/**
     * Returns whether the instruction at the given offset is part of a
     * subroutine.
     */
public boolean isSubroutine(int instructionOffset) {
    return branchTargetFinder.isSubroutine(instructionOffset);
}
