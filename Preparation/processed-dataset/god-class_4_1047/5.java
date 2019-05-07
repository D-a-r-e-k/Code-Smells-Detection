/**
     * Marks the variable and its producing instructions at the given offsets.
     * @param producerOffsets the offsets of the producers to be marked.
     * @param variableIndex   the index of the variable to be marked.
     */
private void markVariableProducers(InstructionOffsetValue producerOffsets, int variableIndex) {
    if (producerOffsets != null) {
        int offsetCount = producerOffsets.instructionOffsetCount();
        for (int offsetIndex = 0; offsetIndex < offsetCount; offsetIndex++) {
            // Make sure the variable and the instruction are marked 
            // at the producing offset. 
            int offset = producerOffsets.instructionOffset(offsetIndex);
            markVariableAfter(offset, variableIndex);
            markInstruction(offset);
        }
    }
}
