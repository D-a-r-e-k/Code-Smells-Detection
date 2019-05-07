/**
     * Evaluates a subroutine and its exception handlers, starting at the given
     * offset and ending at a subroutine return instruction.
     */
private void evaluateSubroutine(Clazz clazz, Method method, CodeAttribute codeAttribute, TracedVariables variables, TracedStack stack, int subroutineStart, java.util.Stack instructionBlockStack) {
    int subroutineEnd = branchTargetFinder.subroutineEnd(subroutineStart);
    if (DEBUG)
        System.out.println("Evaluating subroutine from " + subroutineStart + " to " + subroutineEnd);
    PartialEvaluator subroutinePartialEvaluator = this;
    // Create a temporary partial evaluator if necessary. 
    if (evaluationCounts[subroutineStart] > 0) {
        if (DEBUG)
            System.out.println("Creating new partial evaluator for subroutine");
        subroutinePartialEvaluator = new PartialEvaluator(this);
        subroutinePartialEvaluator.initializeArrays(codeAttribute);
    }
    // Evaluate the subroutine. 
    subroutinePartialEvaluator.evaluateInstructionBlockAndExceptionHandlers(clazz, method, codeAttribute, variables, stack, subroutineStart, subroutineEnd);
    // Merge back the temporary partial evaluator if necessary. 
    if (subroutinePartialEvaluator != this) {
        generalize(subroutinePartialEvaluator, 0, codeAttribute.u4codeLength);
    }
    if (DEBUG)
        System.out.println("Ending subroutine from " + subroutineStart + " to " + subroutineEnd);
}
