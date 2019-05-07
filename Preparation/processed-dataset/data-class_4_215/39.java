private boolean isAnyInstructionNecessary(int instructionOffset1, int instructionOffset2) {
    for (int instructionOffset = instructionOffset1; instructionOffset < instructionOffset2; instructionOffset++) {
        if (isInstructionNecessary(instructionOffset)) {
            return true;
        }
    }
    return false;
}
