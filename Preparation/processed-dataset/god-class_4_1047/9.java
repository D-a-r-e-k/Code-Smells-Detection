/**
     * Marks the stack entry and its producing instructions at the given
     * offsets.
     * @param producerOffsets the offsets of the producers to be marked.
     * @param stackIndex      the index of the stack entry to be marked
     *                        (counting from the bottom).
     */
private void markStackEntryProducers(InstructionOffsetValue producerOffsets, int stackIndex) {
    if (producerOffsets != null) {
        int offsetCount = producerOffsets.instructionOffsetCount();
        for (int offsetIndex = 0; offsetIndex < offsetCount; offsetIndex++) {
            // Make sure the stack entry and the instruction are marked 
            // at the producing offset. 
            int offset = producerOffsets.instructionOffset(offsetIndex);
            markStackEntryAfter(offset, stackIndex);
            markInstruction(offset);
        }
    }
}
