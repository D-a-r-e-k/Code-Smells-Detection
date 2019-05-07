private boolean isInstructionNecessary(int instructionOffset) {
    return instructionOffset == PartialEvaluator.AT_METHOD_ENTRY || instructionsNecessary[instructionOffset];
}
