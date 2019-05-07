/**
     * Ensures that a new line has been started.
     */
protected void ensureNewLine() {
    try {
        if (lastElementType == Element.PHRASE || lastElementType == Element.CHUNK) {
            newLine();
            flushLines();
        }
    } catch (DocumentException ex) {
        throw new ExceptionConverter(ex);
    }
}
