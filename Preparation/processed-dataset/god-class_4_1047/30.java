/**
     * Returns whether the specified variable is ever necessary after any
     * instructions in the specified set of instructions offsets.
     */
private boolean isVariableNecessaryAfterAny(InstructionOffsetValue instructionOffsetValue, int variableIndex) {
    int count = instructionOffsetValue.instructionOffsetCount();
    for (int index = 0; index < count; index++) {
        if (isVariableNecessaryAfter(instructionOffsetValue.instructionOffset(index), variableIndex)) {
            return true;
        }
    }
    return false;
}
