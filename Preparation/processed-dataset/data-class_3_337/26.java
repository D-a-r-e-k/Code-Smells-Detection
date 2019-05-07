/** Comments out a single line with wing comments -- "// ".  Assumes that _currentLocation is the beginning of the
    * line to be commented out.  Only runs in event thread. */
private void _commentLine() {
    // Insert "// " at the beginning of the line. 
    // Using null for AttributeSet follows convention in this class. 
    try {
        insertString(_currentLocation, "//", null);
    } catch (BadLocationException e) {
        throw new UnexpectedException(e);
    }
}
