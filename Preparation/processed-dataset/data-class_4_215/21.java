/**
     * Returns whether any traced but unnecessary instruction between the two
     * given offsets is branching over the second given offset.
     */
private boolean isAnyUnnecessaryInstructionBranchingOver(int instructionOffset1, int instructionOffset2) {
    for (int offset = instructionOffset1; offset < instructionOffset2; offset++) {
        // Is it a traced but unmarked straddling branch? 
        if (partialEvaluator.isTraced(offset) && !isInstructionNecessary(offset) && isAnyLargerThan(partialEvaluator.branchTargets(offset), instructionOffset2)) {
            return true;
        }
    }
    return false;
}
