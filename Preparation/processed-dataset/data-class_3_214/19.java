/**
     * Returns the stack before execution of the instruction at the given
     * offset.
     */
public TracedStack getStackBefore(int instructionOffset) {
    return stacksBefore[instructionOffset];
}
