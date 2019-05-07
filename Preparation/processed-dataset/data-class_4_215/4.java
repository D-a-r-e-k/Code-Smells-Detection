// Small utility methods. 
/**
     * Marks the variable and the corresponding producing instructions
     * of the consumer at the given offset.
     * @param consumerOffset the offset of the consumer.
     * @param variableIndex  the index of the variable to be marked.
     */
private void markVariableProducers(int consumerOffset, int variableIndex) {
    TracedVariables tracedVariables = partialEvaluator.getVariablesBefore(consumerOffset);
    // Mark the producer of the loaded value. 
    markVariableProducers(tracedVariables.getProducerValue(variableIndex).instructionOffsetValue(), variableIndex);
}
