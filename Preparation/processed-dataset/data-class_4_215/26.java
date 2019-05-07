/**
     * Returns whether the specified variable is initialized at the specified
     * offset.
     */
private boolean isVariableInitialization(int instructionOffset, int variableIndex) {
    // Wasn't the variable set yet? 
    Value valueBefore = partialEvaluator.getVariablesBefore(instructionOffset).getValue(variableIndex);
    if (valueBefore == null) {
        return true;
    }
    // Is the computational type different now? 
    Value valueAfter = partialEvaluator.getVariablesAfter(instructionOffset).getValue(variableIndex);
    if (valueAfter.computationalType() != valueBefore.computationalType()) {
        return true;
    }
    // Was the producer an argument (which may be removed)? 
    Value producersBefore = partialEvaluator.getVariablesBefore(instructionOffset).getProducerValue(variableIndex);
    return producersBefore.instructionOffsetValue().instructionOffsetCount() == 1 && producersBefore.instructionOffsetValue().instructionOffset(0) == PartialEvaluator.AT_METHOD_ENTRY;
}
