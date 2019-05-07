/**
     * Returns whether the specified variable is ever necessary after all
     * instructions in the specified set of instructions offsets.
     */
private boolean isVariableNecessaryAfterAll(InstructionOffsetValue instructionOffsetValue, int variableIndex) {
    int count = instructionOffsetValue.instructionOffsetCount();
    for (int index = 0; index < count; index++) {
        if (!isVariableNecessaryAfter(instructionOffsetValue.instructionOffset(index), variableIndex)) {
            return false;
        }
    }
    return true;
}
