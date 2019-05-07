/**
     * Marks the stack entry and the corresponding producing instructions
     * of the consumer at the given offset.
     * @param consumerOffset the offset of the consumer.
     * @param stackIndex     the index of the stack entry to be marked
     *                        (counting from the top).
     */
private void markStackEntryProducers(int consumerOffset, int stackIndex) {
    TracedStack tracedStack = partialEvaluator.getStackBefore(consumerOffset);
    int stackBottomIndex = tracedStack.size() - 1 - stackIndex;
    if (!isStackSimplifiedBefore(consumerOffset, stackBottomIndex)) {
        markStackEntryProducers(tracedStack.getTopProducerValue(stackIndex).instructionOffsetValue(), stackBottomIndex);
    }
}
