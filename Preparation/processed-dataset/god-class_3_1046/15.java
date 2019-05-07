/**
     * Returns the instruction offset at which this initializer is calling
     * the "super" or "this" initializer method, or <code>NONE</code> if it is
     * not an initializer.
     */
public int superInitializationOffset() {
    return branchTargetFinder.superInitializationOffset();
}
