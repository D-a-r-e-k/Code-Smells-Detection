public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
    //        DEBUG = DEBUG_RESULTS = 
    //            clazz.getName().equals("abc/Def") && 
    //            method.getName(clazz).equals("abc"); 
    // TODO: Remove this when the partial evaluator has stabilized. 
    // Catch any unexpected exceptions from the actual visiting method. 
    try {
        // Process the code. 
        visitCodeAttribute0(clazz, method, codeAttribute);
    } catch (RuntimeException ex) {
        System.err.println("Unexpected error while performing partial evaluation:");
        System.err.println("  Class       = [" + clazz.getName() + "]");
        System.err.println("  Method      = [" + method.getName(clazz) + method.getDescriptor(clazz) + "]");
        System.err.println("  Exception   = [" + ex.getClass().getName() + "] (" + ex.getMessage() + ")");
        //if (DEBUG) 
        {
            method.accept(clazz, new ClassPrinter());
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
        throw ex;
    }
}
