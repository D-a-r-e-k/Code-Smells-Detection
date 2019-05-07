public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
    byte opcode = constantInstruction.opcode;
    // Check for instructions that involve fields. 
    switch(opcode) {
        case InstructionConstants.OP_LDC:
        case InstructionConstants.OP_LDC_W:
            // Mark the field, if any, as being read from and written to. 
            reading = true;
            writing = true;
            clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
            break;
        case InstructionConstants.OP_GETSTATIC:
        case InstructionConstants.OP_GETFIELD:
            // Mark the field as being read from. 
            reading = true;
            writing = false;
            clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
            break;
        case InstructionConstants.OP_PUTSTATIC:
        case InstructionConstants.OP_PUTFIELD:
            // Mark the field as being written to. 
            reading = false;
            writing = true;
            clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
            break;
    }
}
