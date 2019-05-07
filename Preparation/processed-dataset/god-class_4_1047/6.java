/**
     * Marks the stack entries and their producing instructions of the
     * consumer at the given offset.
     * @param clazz          the containing class.
     * @param consumerOffset the offset of the consumer.
     * @param consumer       the consumer of the stack entries.
     */
private void markStackProducers(Clazz clazz, int consumerOffset, Instruction consumer) {
    // Mark the producers of the popped values. 
    int popCount = consumer.stackPopCount(clazz);
    for (int stackIndex = 0; stackIndex < popCount; stackIndex++) {
        markStackEntryProducers(consumerOffset, stackIndex);
    }
}
