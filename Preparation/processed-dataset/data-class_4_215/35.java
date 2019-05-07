private boolean isStackEntryNecessaryAfter(int instructionOffset, int stackIndex) {
    return instructionOffset == PartialEvaluator.AT_CATCH_ENTRY || stacksNecessaryAfter[instructionOffset][stackIndex];
}
