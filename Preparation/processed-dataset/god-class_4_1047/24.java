/**
     * Initializes the necessary data structure.
     */
private void initializeNecessary(CodeAttribute codeAttribute) {
    int codeLength = codeAttribute.u4codeLength;
    int maxLocals = codeAttribute.u2maxLocals;
    int maxStack = codeAttribute.u2maxStack;
    // Create new arrays for storing information at each instruction offset. 
    if (variablesNecessaryAfter.length < codeLength || variablesNecessaryAfter[0].length < maxLocals) {
        variablesNecessaryAfter = new boolean[codeLength][maxLocals];
    } else {
        for (int offset = 0; offset < codeLength; offset++) {
            for (int index = 0; index < maxLocals; index++) {
                variablesNecessaryAfter[offset][index] = false;
            }
        }
    }
    if (stacksNecessaryAfter.length < codeLength || stacksNecessaryAfter[0].length < maxStack) {
        stacksNecessaryAfter = new boolean[codeLength][maxStack];
    } else {
        for (int offset = 0; offset < codeLength; offset++) {
            for (int index = 0; index < maxStack; index++) {
                stacksNecessaryAfter[offset][index] = false;
            }
        }
    }
    if (stacksSimplifiedBefore.length < codeLength || stacksSimplifiedBefore[0].length < maxStack) {
        stacksSimplifiedBefore = new boolean[codeLength][maxStack];
    } else {
        for (int offset = 0; offset < codeLength; offset++) {
            for (int index = 0; index < maxStack; index++) {
                stacksSimplifiedBefore[offset][index] = false;
            }
        }
    }
    if (instructionsNecessary.length < codeLength) {
        instructionsNecessary = new boolean[codeLength];
    } else {
        for (int index = 0; index < codeLength; index++) {
            instructionsNecessary[index] = false;
        }
    }
}
