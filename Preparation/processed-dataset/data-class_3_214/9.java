/**
     * Returns whether the instruction at the given offset is a subroutine
     * invocation.
     */
public boolean isSubroutineInvocation(int instructionOffset) {
    return branchTargetFinder.isSubroutineInvocation(instructionOffset);
}
