/**
     * Evaluates a block of instructions, starting at the given offset and ending
     * at a branch instruction, a return instruction, or a throw instruction.
     * Instruction blocks that are to be evaluated as a result are pushed on
     * the given stack.
     */
private void evaluateSingleInstructionBlock(Clazz clazz, Method method, CodeAttribute codeAttribute, TracedVariables variables, TracedStack stack, int startOffset) {
    byte[] code = codeAttribute.code;
    if (DEBUG) {
        System.out.println("Instruction block starting at [" + startOffset + "] in " + ClassUtil.externalFullMethodDescription(clazz.getName(), 0, method.getName(clazz), method.getDescriptor(clazz)));
        System.out.println("Init vars:  " + variables);
        System.out.println("Init stack: " + stack);
    }
    Processor processor = new Processor(variables, stack, valueFactory, branchUnit, invocationUnit);
    int instructionOffset = startOffset;
    int maxOffset = startOffset;
    // Evaluate the subsequent instructions. 
    while (true) {
        if (maxOffset < instructionOffset) {
            maxOffset = instructionOffset;
        }
        // Maintain a generalized local variable frame and stack at this 
        // instruction offset, before execution. 
        int evaluationCount = evaluationCounts[instructionOffset];
        if (evaluationCount == 0) {
            // First time we're passing by this instruction. 
            if (variablesBefore[instructionOffset] == null) {
                // There's not even a context at this index yet. 
                variablesBefore[instructionOffset] = new TracedVariables(variables);
                stacksBefore[instructionOffset] = new TracedStack(stack);
            } else {
                // Reuse the context objects at this index. 
                variablesBefore[instructionOffset].initialize(variables);
                stacksBefore[instructionOffset].copy(stack);
            }
            // We'll execute in the generalized context, because it is 
            // the same as the current context. 
            generalizedContexts[instructionOffset] = true;
        } else {
            // Merge in the current context. 
            boolean variablesChanged = variablesBefore[instructionOffset].generalize(variables, true);
            boolean stackChanged = stacksBefore[instructionOffset].generalize(stack);
            //System.out.println("GVars:  "+variablesBefore[instructionOffset]); 
            //System.out.println("GStack: "+stacksBefore[instructionOffset]); 
            // Bail out if the current context is the same as last time. 
            if (!variablesChanged && !stackChanged && generalizedContexts[instructionOffset]) {
                if (DEBUG)
                    System.out.println("Repeated variables, stack, and branch targets");
                break;
            }
            // See if this instruction has been evaluated an excessive number 
            // of times. 
            if (evaluationCount >= MAXIMUM_EVALUATION_COUNT) {
                if (DEBUG)
                    System.out.println("Generalizing current context after " + evaluationCount + " evaluations");
                // Continue, but generalize the current context. 
                // Note that the most recent variable values have to remain 
                // last in the generalizations, for the sake of the ret 
                // instruction. 
                variables.generalize(variablesBefore[instructionOffset], false);
                stack.generalize(stacksBefore[instructionOffset]);
                // We'll execute in the generalized context. 
                generalizedContexts[instructionOffset] = true;
            } else {
                // We'll execute in the current context. 
                generalizedContexts[instructionOffset] = false;
            }
        }
        // We'll evaluate this instruction. 
        evaluationCounts[instructionOffset]++;
        // Remember this instruction's offset with any stored value. 
        Value storeValue = new InstructionOffsetValue(instructionOffset);
        variables.setProducerValue(storeValue);
        stack.setProducerValue(storeValue);
        // Reset the trace value. 
        InstructionOffsetValue traceValue = InstructionOffsetValue.EMPTY_VALUE;
        // Note that the instruction is only volatile. 
        Instruction instruction = InstructionFactory.create(code, instructionOffset);
        // By default, the next instruction will be the one after this 
        // instruction. 
        int nextInstructionOffset = instructionOffset + instruction.length(instructionOffset);
        InstructionOffsetValue nextInstructionOffsetValue = new InstructionOffsetValue(nextInstructionOffset);
        branchUnit.resetCalled();
        branchUnit.setTraceBranchTargets(nextInstructionOffsetValue);
        if (DEBUG) {
            System.out.println(instruction.toString(instructionOffset));
        }
        try {
            // Process the instruction. The processor may modify the 
            // variables and the stack, and it may call the branch unit 
            // and the invocation unit. 
            instruction.accept(clazz, method, codeAttribute, instructionOffset, processor);
        } catch (RuntimeException ex) {
            System.err.println("Unexpected error while evaluating instruction:");
            System.err.println("  Class       = [" + clazz.getName() + "]");
            System.err.println("  Method      = [" + method.getName(clazz) + method.getDescriptor(clazz) + "]");
            System.err.println("  Instruction = " + instruction.toString(instructionOffset));
            System.err.println("  Exception   = [" + ex.getClass().getName() + "] (" + ex.getMessage() + ")");
            throw ex;
        }
        // Collect the branch targets from the branch unit. 
        InstructionOffsetValue branchTargets = branchUnit.getTraceBranchTargets();
        int branchTargetCount = branchTargets.instructionOffsetCount();
        // Stop tracing. 
        branchUnit.setTraceBranchTargets(traceValue);
        if (DEBUG) {
            if (branchUnit.wasCalled()) {
                System.out.println("     is branching to " + branchTargets);
            }
            if (branchTargetValues[instructionOffset] != null) {
                System.out.println("     has up till now been branching to " + branchTargetValues[instructionOffset]);
            }
            System.out.println(" Vars:  " + variables);
            System.out.println(" Stack: " + stack);
        }
        // Maintain a generalized local variable frame and stack at this 
        // instruction offset, after execution. 
        if (evaluationCount == 0) {
            // First time we're passing by this instruction. 
            if (variablesAfter[instructionOffset] == null) {
                // There's not even a context at this index yet. 
                variablesAfter[instructionOffset] = new TracedVariables(variables);
                stacksAfter[instructionOffset] = new TracedStack(stack);
            } else {
                // Reuse the context objects at this index. 
                variablesAfter[instructionOffset].initialize(variables);
                stacksAfter[instructionOffset].copy(stack);
            }
        } else {
            // Merge in the current context. 
            variablesAfter[instructionOffset].generalize(variables, true);
            stacksAfter[instructionOffset].generalize(stack);
        }
        // Did the branch unit get called? 
        if (branchUnit.wasCalled()) {
            // Accumulate the branch targets at this offset. 
            branchTargetValues[instructionOffset] = branchTargetValues[instructionOffset] == null ? branchTargets : branchTargetValues[instructionOffset].generalize(branchTargets).instructionOffsetValue();
            // Are there no branch targets at all? 
            if (branchTargetCount == 0) {
                // Exit from this code block. 
                break;
            }
            // Accumulate the branch origins at the branch target offsets. 
            InstructionOffsetValue instructionOffsetValue = new InstructionOffsetValue(instructionOffset);
            for (int index = 0; index < branchTargetCount; index++) {
                int branchTarget = branchTargets.instructionOffset(index);
                branchOriginValues[branchTarget] = branchOriginValues[branchTarget] == null ? instructionOffsetValue : branchOriginValues[branchTarget].generalize(instructionOffsetValue).instructionOffsetValue();
            }
            // Are there multiple branch targets? 
            if (branchTargetCount > 1) {
                // Push them on the execution stack and exit from this block. 
                for (int index = 0; index < branchTargetCount; index++) {
                    if (DEBUG)
                        System.out.println("Pushing alternative branch #" + index + " out of " + branchTargetCount + ", from [" + instructionOffset + "] to [" + branchTargets.instructionOffset(index) + "]");
                    pushInstructionBlock(new TracedVariables(variables), new TracedStack(stack), branchTargets.instructionOffset(index));
                }
                break;
            }
            if (DEBUG)
                System.out.println("Definite branch from [" + instructionOffset + "] to [" + branchTargets.instructionOffset(0) + "]");
        }
        // Just continue with the next instruction. 
        instructionOffset = branchTargets.instructionOffset(0);
        // Is this a subroutine invocation? 
        if (instruction.opcode == InstructionConstants.OP_JSR || instruction.opcode == InstructionConstants.OP_JSR_W) {
            // Evaluate the subroutine, possibly in another partial 
            // evaluator. 
            evaluateSubroutine(clazz, method, codeAttribute, variables, stack, instructionOffset, instructionBlockStack);
            break;
        } else if (instruction.opcode == InstructionConstants.OP_RET) {
            // Let the partial evaluator that has called the subroutine 
            // handle the evaluation after the return. 
            pushCallingInstructionBlock(new TracedVariables(variables), new TracedStack(stack), instructionOffset);
            break;
        }
    }
    if (DEBUG)
        System.out.println("Ending processing of instruction block starting at [" + startOffset + "]");
}
