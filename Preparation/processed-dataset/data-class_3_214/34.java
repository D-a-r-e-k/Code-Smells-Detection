/**
     * Generalize the local variable frames of a block of instructions.
     */
private void generalizeVariables(int startOffset, int endOffset, boolean includeAfterLastInstruction, TracedVariables generalizedVariables) {
    boolean first = true;
    int lastIndex = -1;
    // Generalize the variables before each of the instructions in the block. 
    for (int index = startOffset; index < endOffset; index++) {
        if (isTraced(index)) {
            TracedVariables tracedVariables = variablesBefore[index];
            if (first) {
                // Initialize the variables with the first traced local 
                // variable frame. 
                generalizedVariables.initialize(tracedVariables);
                first = false;
            } else {
                // Generalize the variables with the traced local variable 
                // frame. We can't use the return value, because local 
                // generalization can be different a couple of times, 
                // with the global generalization being the same. 
                generalizedVariables.generalize(tracedVariables, false);
            }
            lastIndex = index;
        }
    }
    // Generalize the variables after the last instruction in the block, 
    // if required. 
    if (includeAfterLastInstruction && lastIndex >= 0) {
        TracedVariables tracedVariables = variablesAfter[lastIndex];
        if (first) {
            // Initialize the variables with the local variable frame. 
            generalizedVariables.initialize(tracedVariables);
        } else {
            // Generalize the variables with the local variable frame. 
            generalizedVariables.generalize(tracedVariables, false);
        }
    }
    // Just clear the variables if there aren't any traced instructions 
    // in the block. 
    if (first) {
        generalizedVariables.reset(generalizedVariables.size());
    }
}
