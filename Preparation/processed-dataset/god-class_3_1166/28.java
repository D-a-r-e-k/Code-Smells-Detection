/** Uncomments all lines between start and end inclusive. 
    * @param start Position in document to start commenting from
    * @param end Position in document to end commenting at
    */
private int uncommentBlock(final int start, final int end) {
    int afterUncommentEnd = end;
    try {
        // Keep marker at the end. This Position will be the correct endpoint no matter how we change the doc 
        // doing the indentLine calls. 
        final Position endPos = this.createUnwrappedPosition(end);
        // Iterate, line by line, until we get to/past the end 
        int walker = _getLineStartPos(start);
        //      Utilities.show("Initial walker pos = " + walker); 
        while (walker < endPos.getOffset()) {
            setCurrentLocation(walker);
            // Move cursor to walker position 
            int diff = _uncommentLine();
            // Uncomment current line, accessing the reduced model 
            afterUncommentEnd -= diff;
            // Update afterUncommentEnd 
            walker = _getLineEndPos(walker) + 1;
        }
    } catch (BadLocationException e) {
        throw new UnexpectedException(e);
    }
    return afterUncommentEnd;
}
