private void markStraddlingBranch(int instructionOffsetStart, int instructionOffsetEnd, int branchOrigin, int branchTarget) {
    if (!isInstructionNecessary(branchOrigin) && isAnyInstructionNecessary(instructionOffsetStart, instructionOffsetEnd)) {
        if (DEBUG)
            System.out.print("[" + branchOrigin + "->" + branchTarget + "]");
        // Mark the branch instruction. 
        markInstruction(branchOrigin);
    }
}
