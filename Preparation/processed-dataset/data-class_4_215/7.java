/**
     * Marks the stack entry and the corresponding producing instructions
     * of the consumer at the given offset, if the stack entry of the
     * consumer is marked.
     * @param consumerOffset     the offset of the consumer.
     * @param consumerStackIndex the index of the stack entry to be checked
     *                           (counting from the top).
     * @param producerStackIndex the index of the stack entry to be marked
     *                           (counting from the top).
     */
private void conditionallyMarkStackEntryProducers(int consumerOffset, int consumerStackIndex, int producerStackIndex) {
    int top = partialEvaluator.getStackAfter(consumerOffset).size() - 1;
    if (isStackEntryNecessaryAfter(consumerOffset, top - consumerStackIndex)) {
        markStackEntryProducers(consumerOffset, producerStackIndex);
    }
}
