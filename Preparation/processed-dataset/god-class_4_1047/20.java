/**
     * Returns whether the given instruction is a pop instruction
     * (pop, pop2).
     */
private boolean isPop(Instruction instruction) {
    return instruction.opcode == InstructionConstants.OP_POP || instruction.opcode == InstructionConstants.OP_POP2;
}
