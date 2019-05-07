/**
     * Returns the instruction offsets that branch to the given instruction
     * offset.
     */
public InstructionOffsetValue branchOrigins(int instructionOffset) {
    return branchOriginValues[instructionOffset];
}
