/**
     * Returns the opcode of a push instruction corresponding to the given
     * computational type.
     * @param computationalType the computational type to be pushed on the stack.
     */
private byte pushOpcode(int computationalType) {
    switch(computationalType) {
        case Value.TYPE_INTEGER:
            return InstructionConstants.OP_ICONST_0;
        case Value.TYPE_LONG:
            return InstructionConstants.OP_LCONST_0;
        case Value.TYPE_FLOAT:
            return InstructionConstants.OP_FCONST_0;
        case Value.TYPE_DOUBLE:
            return InstructionConstants.OP_DCONST_0;
        case Value.TYPE_REFERENCE:
        case Value.TYPE_INSTRUCTION_OFFSET:
            return InstructionConstants.OP_ACONST_NULL;
    }
    throw new IllegalArgumentException("No push opcode for computational type [" + computationalType + "]");
}
