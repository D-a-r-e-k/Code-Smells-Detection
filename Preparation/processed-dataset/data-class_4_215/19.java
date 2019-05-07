// Small utility methods. 
/**
     * Returns whether the given instruction is a dup or swap instruction
     * (dup, dup_x1, dup_x2, dup2, dup2_x1, dup2_x2, swap).
     */
private boolean isDupOrSwap(Instruction instruction) {
    return instruction.opcode >= InstructionConstants.OP_DUP && instruction.opcode <= InstructionConstants.OP_SWAP;
}
