public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction) {
    byte opcode = simpleInstruction.opcode;
    // Check for instructions that might cause side effects. 
    if (opcode == InstructionConstants.OP_IASTORE || opcode == InstructionConstants.OP_LASTORE || opcode == InstructionConstants.OP_FASTORE || opcode == InstructionConstants.OP_DASTORE || opcode == InstructionConstants.OP_AASTORE || opcode == InstructionConstants.OP_BASTORE || opcode == InstructionConstants.OP_CASTORE || opcode == InstructionConstants.OP_SASTORE || opcode == InstructionConstants.OP_ATHROW || opcode == InstructionConstants.OP_MONITORENTER || opcode == InstructionConstants.OP_MONITOREXIT || (includeReturnInstructions && (opcode == InstructionConstants.OP_IRETURN || opcode == InstructionConstants.OP_LRETURN || opcode == InstructionConstants.OP_FRETURN || opcode == InstructionConstants.OP_DRETURN || opcode == InstructionConstants.OP_ARETURN || opcode == InstructionConstants.OP_RETURN))) {
        // These instructions always cause a side effect. 
        hasSideEffects = true;
    }
}
