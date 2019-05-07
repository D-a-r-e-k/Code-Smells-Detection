/**
     * Evaluates the exception handlers covering and targeting the given
     * instruction range in the given code.
     */
private void evaluateExceptionHandlers(Clazz clazz, Method method, CodeAttribute codeAttribute, int startOffset, int endOffset) {
    if (DEBUG)
        System.out.println("Evaluating exceptions covering [" + startOffset + " -> " + endOffset + "]:");
    ExceptionHandlerFilter exceptionEvaluator = new ExceptionHandlerFilter(startOffset, endOffset, this);
    // Evaluate the exception catch blocks, until their entry variables 
    // have stabilized. 
    do {
        // Reset the flag to stop evaluating. 
        evaluateExceptions = false;
        // Evaluate all relevant exception catch blocks once. 
        codeAttribute.exceptionsAccept(clazz, method, startOffset, endOffset, exceptionEvaluator);
    } while (evaluateExceptions);
}
