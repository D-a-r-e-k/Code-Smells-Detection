private void markVariableAfter(int instructionOffset, int variableIndex) {
    if (!isVariableNecessaryAfter(instructionOffset, variableIndex)) {
        if (DEBUG)
            System.out.print("[" + instructionOffset + ".v" + variableIndex + "],");
        variablesNecessaryAfter[instructionOffset][variableIndex] = true;
        if (maxMarkedOffset < instructionOffset) {
            maxMarkedOffset = instructionOffset;
        }
    }
}
