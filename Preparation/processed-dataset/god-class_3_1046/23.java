// Utility methods to evaluate instruction blocks. 
/**
     * Pushes block of instructions to be executed in the calling partial
     * evaluator.
     */
private void pushCallingInstructionBlock(TracedVariables variables, TracedStack stack, int startOffset) {
    callingInstructionBlockStack.push(new MyInstructionBlock(variables, stack, startOffset));
}
