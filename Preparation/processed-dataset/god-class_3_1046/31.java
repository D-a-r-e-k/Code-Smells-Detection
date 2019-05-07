// Implementations for ExceptionInfoVisitor. 
public void visitExceptionInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, ExceptionInfo exceptionInfo) {
    int startPC = exceptionInfo.u2startPC;
    int endPC = exceptionInfo.u2endPC;
    // Do we have to evaluate this exception catch block? 
    if (isTraced(startPC, endPC)) {
        int handlerPC = exceptionInfo.u2handlerPC;
        int catchType = exceptionInfo.u2catchType;
        if (DEBUG)
            System.out.println("Evaluating exception [" + startPC + " -> " + endPC + ": " + handlerPC + "]:");
        // Reuse the existing variables and stack objects, ensuring the 
        // right size. 
        TracedVariables variables = new TracedVariables(codeAttribute.u2maxLocals);
        TracedStack stack = new TracedStack(codeAttribute.u2maxStack);
        // Initialize the trace values. 
        Value storeValue = new InstructionOffsetValue(AT_CATCH_ENTRY);
        variables.setProducerValue(storeValue);
        stack.setProducerValue(storeValue);
        // Initialize the variables by generalizing the variables of the 
        // try block. Make sure to include the results of the last 
        // instruction for preverification. 
        generalizeVariables(startPC, endPC, evaluateAllCode, variables);
        // Initialize the the stack. 
        //stack.push(valueFactory.createReference((ClassConstant)((ProgramClass)clazz).getConstant(exceptionInfo.u2catchType), false)); 
        String catchClassName = catchType != 0 ? clazz.getClassName(catchType) : ClassConstants.INTERNAL_NAME_JAVA_LANG_THROWABLE;
        Clazz catchClass = catchType != 0 ? ((ClassConstant) ((ProgramClass) clazz).getConstant(catchType)).referencedClass : null;
        stack.push(valueFactory.createReferenceValue(catchClassName, catchClass, false));
        int evaluationCount = evaluationCounts[handlerPC];
        // Evaluate the instructions, starting at the entry point. 
        evaluateInstructionBlock(clazz, method, codeAttribute, variables, stack, handlerPC);
        // Remember to evaluate all exception handlers once more. 
        if (!evaluateExceptions) {
            evaluateExceptions = evaluationCount < evaluationCounts[handlerPC];
        }
    } else {
        if (DEBUG)
            System.out.println("No information for partial evaluation of exception [" + startPC + " -> " + endPC + ": " + exceptionInfo.u2handlerPC + "]");
    }
}
