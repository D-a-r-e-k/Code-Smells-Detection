/**
     * Generalizes the results of this partial evaluator with those of another
     * given partial evaluator, over a given range of instructions.
     */
private void generalize(PartialEvaluator other, int codeStart, int codeEnd) {
    if (DEBUG)
        System.out.println("Generalizing with temporary partial evaluation");
    for (int offset = codeStart; offset < codeEnd; offset++) {
        if (other.branchOriginValues[offset] != null) {
            branchOriginValues[offset] = branchOriginValues[offset] == null ? other.branchOriginValues[offset] : branchOriginValues[offset].generalize(other.branchOriginValues[offset]).instructionOffsetValue();
        }
        if (other.isTraced(offset)) {
            if (other.branchTargetValues[offset] != null) {
                branchTargetValues[offset] = branchTargetValues[offset] == null ? other.branchTargetValues[offset] : branchTargetValues[offset].generalize(other.branchTargetValues[offset]).instructionOffsetValue();
            }
            if (evaluationCounts[offset] == 0) {
                variablesBefore[offset] = other.variablesBefore[offset];
                stacksBefore[offset] = other.stacksBefore[offset];
                variablesAfter[offset] = other.variablesAfter[offset];
                stacksAfter[offset] = other.stacksAfter[offset];
                generalizedContexts[offset] = other.generalizedContexts[offset];
                evaluationCounts[offset] = other.evaluationCounts[offset];
            } else {
                variablesBefore[offset].generalize(other.variablesBefore[offset], false);
                stacksBefore[offset].generalize(other.stacksBefore[offset]);
                variablesAfter[offset].generalize(other.variablesAfter[offset], false);
                stacksAfter[offset].generalize(other.stacksAfter[offset]);
                //generalizedContexts[offset] 
                evaluationCounts[offset] += other.evaluationCounts[offset];
            }
        }
    }
}
