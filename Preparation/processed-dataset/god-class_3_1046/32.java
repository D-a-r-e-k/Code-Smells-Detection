// Small utility methods. 
/**
     * Initializes the data structures for the variables, stack, etc.
     */
private void initializeArrays(CodeAttribute codeAttribute) {
    int codeLength = codeAttribute.u4codeLength;
    // Create new arrays for storing information at each instruction offset. 
    if (variablesAfter.length < codeLength) {
        // Create new arrays. 
        branchOriginValues = new InstructionOffsetValue[codeLength];
        branchTargetValues = new InstructionOffsetValue[codeLength];
        variablesBefore = new TracedVariables[codeLength];
        stacksBefore = new TracedStack[codeLength];
        variablesAfter = new TracedVariables[codeLength];
        stacksAfter = new TracedStack[codeLength];
        generalizedContexts = new boolean[codeLength];
        evaluationCounts = new int[codeLength];
    } else {
        // Reset the arrays. 
        for (int index = 0; index < codeLength; index++) {
            branchOriginValues[index] = null;
            branchTargetValues[index] = null;
            generalizedContexts[index] = false;
            evaluationCounts[index] = 0;
            if (variablesBefore[index] != null) {
                variablesBefore[index].reset(codeAttribute.u2maxLocals);
            }
            if (stacksBefore[index] != null) {
                stacksBefore[index].reset(codeAttribute.u2maxStack);
            }
            if (variablesAfter[index] != null) {
                variablesAfter[index].reset(codeAttribute.u2maxLocals);
            }
            if (stacksAfter[index] != null) {
                stacksAfter[index].reset(codeAttribute.u2maxStack);
            }
        }
    }
}
