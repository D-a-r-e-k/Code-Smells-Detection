/**
     * Returns the offset of the 'new' instruction that corresponds to the
     * invocation of the instance initializer at the given offset, or
     * <code>AT_METHOD_ENTRY</code> if the invocation is calling the "super" or
     * "this" initializer method, , or <code>NONE</code> if it is not a 'new'
     * instruction.
     */
public int creationOffset(int offset) {
    return branchTargetFinder.creationOffset(offset);
}
