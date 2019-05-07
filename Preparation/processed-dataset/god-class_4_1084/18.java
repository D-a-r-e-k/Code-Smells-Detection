/**
     * Gets the current vertical page position.
     * @param ensureNewLine Tells whether a new line shall be enforced. This may cause side effects
     *   for elements that do not terminate the lines they've started because those lines will get
     *   terminated.
     * @return The current vertical page position.
     */
public float getVerticalPosition(boolean ensureNewLine) {
    // ensuring that a new line has been started. 
    if (ensureNewLine) {
        ensureNewLine();
    }
    return top() - currentHeight - indentation.indentTop;
}
