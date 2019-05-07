/** Comments out the lines between start and end inclusive, using wing comments -- "// ".
    * @param start Position in document to start commenting from
    * @param end Position in document to end commenting at
    */
private int commentBlock(final int start, final int end) {
    int afterCommentEnd = end;
    try {
        // Keep marker at the end. This Position will be the correct endpoint no matter how we change the doc doing the 
        // indentLine calls. 
        final Position endPos = this.createUnwrappedPosition(end);
        // Iterate, line by line, until we get to/past the end 
        int walker = _getLineStartPos(start);
        while (walker < endPos.getOffset()) {
            setCurrentLocation(walker);
            // Update cursor 
            _commentLine();
            // Comment out current line; must be atomic 
            afterCommentEnd += WING_COMMENT_OFFSET;
            walker = walker + 2;
            // Skip over inserted slashes; getDistToNewline(walker) = 0 if not advanced 
            setCurrentLocation(walker);
            // reset currentLocation to position past newline 
            // Adding 1 makes us point to the first character AFTER the next newline. 
            walker += _reduced.getDistToNextNewline() + 1;
        }
    } catch (BadLocationException e) {
        throw new UnexpectedException(e);
    }
    return afterCommentEnd;
}
