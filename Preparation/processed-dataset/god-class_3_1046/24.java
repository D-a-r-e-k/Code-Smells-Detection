/**
     * Pushes block of instructions to be executed in this partial evaluator.
     */
private void pushInstructionBlock(TracedVariables variables, TracedStack stack, int startOffset) {
    instructionBlockStack.push(new MyInstructionBlock(variables, stack, startOffset));
}
