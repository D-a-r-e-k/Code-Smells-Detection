/**
     * Returns whether a block of instructions is ever used.
     */
public boolean isTraced(int startOffset, int endOffset) {
    for (int index = startOffset; index < endOffset; index++) {
        if (isTraced(index)) {
            return true;
        }
    }
    return false;
}
