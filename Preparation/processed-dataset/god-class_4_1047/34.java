private boolean isAnyStackEntryNecessaryAfter(InstructionOffsetValue instructionOffsets, int stackIndex) {
    int offsetCount = instructionOffsets.instructionOffsetCount();
    for (int offsetIndex = 0; offsetIndex < offsetCount; offsetIndex++) {
        if (isStackEntryNecessaryAfter(instructionOffsets.instructionOffset(offsetIndex), stackIndex)) {
            return true;
        }
    }
    return false;
}
