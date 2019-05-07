/**
     * Returns the instruction offset at which the object instance that is
     * created at the given 'new' instruction offset is initialized, or
     * <code>NONE</code> if it is not being created.
     */
public int initializationOffset(int instructionOffset) {
    return branchTargetFinder.initializationOffset(instructionOffset);
}
