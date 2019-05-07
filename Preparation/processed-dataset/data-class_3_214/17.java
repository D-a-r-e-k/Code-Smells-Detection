/**
     * Returns the variables before execution of the instruction at the given
     * offset.
     */
public TracedVariables getVariablesBefore(int instructionOffset) {
    return variablesBefore[instructionOffset];
}
