private void markInstruction(int instructionOffset) {
    if (!isInstructionNecessary(instructionOffset)) {
        if (DEBUG)
            System.out.print(instructionOffset + ",");
        instructionsNecessary[instructionOffset] = true;
        if (maxMarkedOffset < instructionOffset) {
            maxMarkedOffset = instructionOffset;
        }
    }
}
