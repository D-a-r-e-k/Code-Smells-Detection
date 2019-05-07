public void visitCodeAttribute0(Clazz clazz, Method method, CodeAttribute codeAttribute) {
    if (DEBUG_RESULTS) {
        System.out.println();
        System.out.println("Class " + ClassUtil.externalClassName(clazz.getName()));
        System.out.println("Method " + ClassUtil.externalFullMethodDescription(clazz.getName(), 0, method.getName(clazz), method.getDescriptor(clazz)));
    }
    // Initialize the necessary array. 
    initializeNecessary(codeAttribute);
    // Evaluate the method. 
    partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);
    int codeLength = codeAttribute.u4codeLength;
    // Reset the code changes. 
    codeAttributeEditor.reset(codeLength);
    // Mark any unused method parameters on the stack. 
    if (DEBUG)
        System.out.println("Invocation simplification:");
    for (int offset = 0; offset < codeLength; offset++) {
        if (partialEvaluator.isTraced(offset)) {
            Instruction instruction = InstructionFactory.create(codeAttribute.code, offset);
            instruction.accept(clazz, method, codeAttribute, offset, unusedParameterSimplifier);
        }
    }
    // Mark all essential instructions that have been encountered as used. 
    if (DEBUG)
        System.out.println("Usage initialization: ");
    maxMarkedOffset = -1;
    // The invocation of the "super" or "this" <init> method inside a 
    // constructor is always necessary. 
    int superInitializationOffset = partialEvaluator.superInitializationOffset();
    if (superInitializationOffset != PartialEvaluator.NONE) {
        if (DEBUG)
            System.out.print("(super.<init>)");
        markInstruction(superInitializationOffset);
    }
    // Also mark infinite loops and instructions that cause side effects. 
    for (int offset = 0; offset < codeLength; offset++) {
        if (partialEvaluator.isTraced(offset)) {
            Instruction instruction = InstructionFactory.create(codeAttribute.code, offset);
            // Mark that the instruction is necessary if it is an infinite loop. 
            if (instruction.opcode == InstructionConstants.OP_GOTO && ((BranchInstruction) instruction).branchOffset == 0) {
                if (DEBUG)
                    System.out.print("(infinite loop)");
                markInstruction(offset);
            } else if (sideEffectInstructionChecker.hasSideEffects(clazz, method, codeAttribute, offset, instruction)) {
                markInstruction(offset);
            }
        }
    }
    if (DEBUG)
        System.out.println();
    // Globally mark instructions and their produced variables and stack 
    // entries on which necessary instructions depend. 
    // Instead of doing this recursively, we loop across all instructions, 
    // starting at the highest previously unmarked instruction that has 
    // been been marked. 
    if (DEBUG)
        System.out.println("Usage marking:");
    while (maxMarkedOffset >= 0) {
        int offset = maxMarkedOffset;
        maxMarkedOffset = offset - 1;
        if (partialEvaluator.isTraced(offset)) {
            if (isInstructionNecessary(offset)) {
                Instruction instruction = InstructionFactory.create(codeAttribute.code, offset);
                instruction.accept(clazz, method, codeAttribute, offset, producerMarker);
            }
            // Check if this instruction is a branch origin from a branch 
            // that straddles some marked code. 
            markStraddlingBranches(offset, partialEvaluator.branchTargets(offset), true);
            // Check if this instruction is a branch target from a branch 
            // that straddles some marked code. 
            markStraddlingBranches(offset, partialEvaluator.branchOrigins(offset), false);
        }
        if (DEBUG) {
            if (maxMarkedOffset > offset) {
                System.out.println(" -> " + maxMarkedOffset);
            }
        }
    }
    if (DEBUG)
        System.out.println();
    // Mark variable initializations, even if they aren't strictly necessary. 
    // The virtual machine's verification step is not smart enough to see 
    // this, and may complain otherwise. 
    if (DEBUG)
        System.out.println("Initialization marking: ");
    for (int offset = 0; offset < codeLength; offset++) {
        // Is it a variable initialization that hasn't been marked yet? 
        if (partialEvaluator.isTraced(offset) && !isInstructionNecessary(offset)) {
            Instruction instruction = InstructionFactory.create(codeAttribute.code, offset);
            instruction.accept(clazz, method, codeAttribute, offset, variableInitializationMarker);
        }
    }
    if (DEBUG)
        System.out.println();
    // Locally fix instructions, in order to keep the stack consistent. 
    if (DEBUG)
        System.out.println("Stack consistency fixing:");
    maxMarkedOffset = codeLength - 1;
    while (maxMarkedOffset >= 0) {
        int offset = maxMarkedOffset;
        maxMarkedOffset = offset - 1;
        if (partialEvaluator.isTraced(offset)) {
            Instruction instruction = InstructionFactory.create(codeAttribute.code, offset);
            instruction.accept(clazz, method, codeAttribute, offset, stackConsistencyFixer);
            // Check if this instruction is a branch origin from a branch 
            // that straddles some marked code. 
            markStraddlingBranches(offset, partialEvaluator.branchTargets(offset), true);
            // Check if this instruction is a branch target from a branch 
            // that straddles some marked code. 
            markStraddlingBranches(offset, partialEvaluator.branchOrigins(offset), false);
        }
    }
    if (DEBUG)
        System.out.println();
    // Replace traced but unmarked backward branches by infinite loops. 
    // The virtual machine's verification step is not smart enough to see 
    // the code isn't reachable, and may complain otherwise. 
    // Any clearly unreachable code will still be removed elsewhere. 
    if (DEBUG)
        System.out.println("Infinite loop fixing:");
    for (int offset = 0; offset < codeLength; offset++) {
        // Is it a traced but unmarked backward branch, without an unmarked 
        // straddling forward branch? Note that this is still a heuristic. 
        if (partialEvaluator.isTraced(offset) && !isInstructionNecessary(offset) && isAllSmallerThanOrEqual(partialEvaluator.branchTargets(offset), offset) && !isAnyUnnecessaryInstructionBranchingOver(lastNecessaryInstructionOffset(offset), offset)) {
            replaceByInfiniteLoop(clazz, offset);
        }
    }
    if (DEBUG)
        System.out.println();
    // Insert infinite loops after jumps to subroutines that don't return. 
    // The virtual machine's verification step is not smart enough to see 
    // the code isn't reachable, and may complain otherwise. 
    if (DEBUG)
        System.out.println("Non-returning subroutine fixing:");
    for (int offset = 0; offset < codeLength; offset++) {
        // Is it a traced but unmarked backward branch, without an unmarked 
        // straddling forward branch? Note that this is still a heuristic. 
        if (isInstructionNecessary(offset) && partialEvaluator.isSubroutineInvocation(offset)) {
            Instruction instruction = InstructionFactory.create(codeAttribute.code, offset);
            int nextOffset = offset + instruction.length(offset);
            if (!isInstructionNecessary(nextOffset)) {
                replaceByInfiniteLoop(clazz, nextOffset);
            }
        }
    }
    if (DEBUG)
        System.out.println();
    // Delete all instructions that are not used. 
    int offset = 0;
    do {
        Instruction instruction = InstructionFactory.create(codeAttribute.code, offset);
        if (!isInstructionNecessary(offset)) {
            codeAttributeEditor.deleteInstruction(offset);
            codeAttributeEditor.insertBeforeInstruction(offset, (Instruction) null);
            codeAttributeEditor.replaceInstruction(offset, (Instruction) null);
            codeAttributeEditor.insertAfterInstruction(offset, (Instruction) null);
            // Visit the instruction, if required. 
            if (extraDeletedInstructionVisitor != null) {
                instruction.accept(clazz, method, codeAttribute, offset, extraDeletedInstructionVisitor);
            }
        }
        offset += instruction.length(offset);
    } while (offset < codeLength);
    if (DEBUG_RESULTS) {
        System.out.println("Simplification results:");
        offset = 0;
        do {
            Instruction instruction = InstructionFactory.create(codeAttribute.code, offset);
            System.out.println((isInstructionNecessary(offset) ? " + " : " - ") + instruction.toString(offset));
            if (partialEvaluator.isTraced(offset)) {
                int initializationOffset = partialEvaluator.initializationOffset(offset);
                if (initializationOffset != PartialEvaluator.NONE) {
                    System.out.println("     is to be initialized at [" + initializationOffset + "]");
                }
                InstructionOffsetValue branchTargets = partialEvaluator.branchTargets(offset);
                if (branchTargets != null) {
                    System.out.println("     has overall been branching to " + branchTargets);
                }
                boolean deleted = codeAttributeEditor.deleted[offset];
                if (isInstructionNecessary(offset) && deleted) {
                    System.out.println("     is deleted");
                }
                Instruction preInsertion = codeAttributeEditor.preInsertions[offset];
                if (preInsertion != null) {
                    System.out.println("     is preceded by: " + preInsertion);
                }
                Instruction replacement = codeAttributeEditor.replacements[offset];
                if (replacement != null) {
                    System.out.println("     is replaced by: " + replacement);
                }
                Instruction postInsertion = codeAttributeEditor.postInsertions[offset];
                if (postInsertion != null) {
                    System.out.println("     is followed by: " + postInsertion);
                }
            }
            offset += instruction.length(offset);
        } while (offset < codeLength);
    }
    // Apply all accumulated changes to the code. 
    codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
}
