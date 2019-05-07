/**
     * Returns whether the specified variable is ever necessary after any
     * instructions in the specified block.
     */
private boolean isVariableNecessaryAfterAny(int startOffset, int endOffset, int variableIndex) {
    for (int offset = startOffset; offset < endOffset; offset++) {
        if (isVariableNecessaryAfter(offset, variableIndex)) {
            return true;
        }
    }
    return false;
}
