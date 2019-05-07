/**
     * Evaluates the instruction block and the exception handlers covering the
     * given instruction range in the given code.
     */
private void evaluateInstructionBlockAndExceptionHandlers(Clazz clazz, Method method, CodeAttribute codeAttribute, TracedVariables variables, TracedStack stack, int startOffset, int endOffset) {
    evaluateInstructionBlock(clazz, method, codeAttribute, variables, stack, startOffset);
    evaluateExceptionHandlers(clazz, method, codeAttribute, startOffset, endOffset);
}
