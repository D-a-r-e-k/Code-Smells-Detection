/**
     * Marks the stack entry and its initializing instruction
     * ('invokespecial *.<init>') for the given 'new' instruction offset.
     * @param newInstructionOffset the offset of the 'new' instruction.
     */
private void markInitialization(int newInstructionOffset) {
    int initializationOffset = partialEvaluator.initializationOffset(newInstructionOffset);
    TracedStack tracedStack = partialEvaluator.getStackAfter(newInstructionOffset);
    markStackEntryAfter(initializationOffset, tracedStack.size() - 1);
    markInstruction(initializationOffset);
}
