/**
     * Evaluates a block of instructions, starting at the given offset and ending
     * at a branch instruction, a return instruction, or a throw instruction.
     */
private void evaluateInstructionBlock(Clazz clazz, Method method, CodeAttribute codeAttribute, TracedVariables variables, TracedStack stack, int startOffset) {
    // Execute the initial instruction block. 
    evaluateSingleInstructionBlock(clazz, method, codeAttribute, variables, stack, startOffset);
    // Execute all resulting instruction blocks on the execution stack. 
    while (!instructionBlockStack.empty()) {
        if (DEBUG)
            System.out.println("Popping alternative branch out of " + instructionBlockStack.size() + " blocks");
        MyInstructionBlock instructionBlock = (MyInstructionBlock) instructionBlockStack.pop();
        evaluateSingleInstructionBlock(clazz, method, codeAttribute, instructionBlock.variables, instructionBlock.stack, instructionBlock.startOffset);
    }
}
