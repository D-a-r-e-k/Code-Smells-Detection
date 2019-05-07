/**
     * Clears the chunk array.
     * A call to <CODE>go()</CODE> will always return NO_MORE_TEXT.
     */
public void clearChunks() {
    if (bidiLine != null)
        bidiLine.clearChunks();
}
