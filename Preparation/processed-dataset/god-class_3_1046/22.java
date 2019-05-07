/**
     * Returns the instruction offsets to which the given instruction offset
     * branches.
     */
public InstructionOffsetValue branchTargets(int instructionOffset) {
    return branchTargetValues[instructionOffset];
}
