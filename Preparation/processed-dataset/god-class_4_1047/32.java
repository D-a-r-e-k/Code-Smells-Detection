private boolean isVariableNecessaryAfter(int instructionOffset, int variableIndex) {
    return instructionOffset == PartialEvaluator.AT_METHOD_ENTRY || variablesNecessaryAfter[instructionOffset][variableIndex];
}
