public void visitCodeAttribute0(Clazz clazz, Method method, CodeAttribute codeAttribute) {
    // Evaluate the instructions, starting at the entry point. 
    if (DEBUG) {
        System.out.println();
        System.out.println("Partial evaluation: " + clazz.getName() + "." + method.getName(clazz) + method.getDescriptor(clazz));
        System.out.println("  Max locals = " + codeAttribute.u2maxLocals);
        System.out.println("  Max stack  = " + codeAttribute.u2maxStack);
    }
    // Reuse the existing variables and stack objects, ensuring the right size. 
    TracedVariables variables = new TracedVariables(codeAttribute.u2maxLocals);
    TracedStack stack = new TracedStack(codeAttribute.u2maxStack);
    // Initialize the reusable arrays and variables. 
    initializeArrays(codeAttribute);
    initializeParameters(clazz, method, codeAttribute, variables);
    // Find all instruction offsets,... 
    codeAttribute.accept(clazz, method, branchTargetFinder);
    // Start executing the first instruction block. 
    evaluateInstructionBlockAndExceptionHandlers(clazz, method, codeAttribute, variables, stack, 0, codeAttribute.u4codeLength);
    if (DEBUG_RESULTS) {
        System.out.println("Evaluation results:");
        int offset = 0;
        do {
            if (isBranchOrExceptionTarget(offset)) {
                System.out.println("Branch target from [" + branchOriginValues[offset] + "]:");
                if (isTraced(offset)) {
                    System.out.println("  Vars:  " + variablesBefore[offset]);
                    System.out.println("  Stack: " + stacksBefore[offset]);
                }
            }
            Instruction instruction = InstructionFactory.create(codeAttribute.code, offset);
            System.out.println(instruction.toString(offset));
            if (isTraced(offset)) {
                int initializationOffset = branchTargetFinder.initializationOffset(offset);
                if (initializationOffset != NONE) {
                    System.out.println("     is to be initialized at [" + initializationOffset + "]");
                }
                InstructionOffsetValue branchTargets = branchTargets(offset);
                if (branchTargets != null) {
                    System.out.println("     has overall been branching to " + branchTargets);
                }
                System.out.println("  Vars:  " + variablesAfter[offset]);
                System.out.println("  Stack: " + stacksAfter[offset]);
            }
            offset += instruction.length(offset);
        } while (offset < codeAttribute.u4codeLength);
    }
}
