/**
     * Returns the stack after execution of the instruction at the given
     * offset.
     */
public TracedStack getStackAfter(int instructionOffset) {
    return stacksAfter[instructionOffset];
}
