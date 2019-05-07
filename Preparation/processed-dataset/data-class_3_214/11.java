/**
     * Returns whether the subroutine at the given offset is ever returning
     * by means of a regular 'ret' instruction.
     */
public boolean isSubroutineReturning(int instructionOffset) {
    return branchTargetFinder.isSubroutineReturning(instructionOffset);
}
