/**
     * Returns the offset after the subroutine that starts at the given
     * offset.
     */
public int subroutineEnd(int instructionOffset) {
    return branchTargetFinder.subroutineEnd(instructionOffset);
}
