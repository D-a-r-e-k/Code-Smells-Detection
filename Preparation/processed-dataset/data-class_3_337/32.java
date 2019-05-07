/** Assuming that text is a document prefix including offset pos, finds the index of the keyword kw
    * searching back from pos.
    */
public int _findPrevKeyword(String text, String kw, int pos) throws BadLocationException {
    /* */
    assert Utilities.TEST_MODE || EventQueue.isDispatchThread();
    int i;
    int reducedPos = pos;
    //    synchronized(_reduced) { 
    final int origLocation = _currentLocation;
    // Move reduced model to location pos 
    _reduced.move(pos - origLocation);
    // reduced model points to pos == reducedPos 
    // Walk backwards from specificed position 
    i = text.lastIndexOf(kw, reducedPos);
    while (i > -1) {
        // Check that this is the beginning of a word 
        if (i > 0) {
            if (Character.isJavaIdentifierPart(text.charAt(i - 1))) {
                // not begining 
                i = text.lastIndexOf(kw, i - 1);
                continue;
            }
        }
        // Check that this not just the beginning of a longer word 
        if (i + kw.length() < text.length()) {
            if (Character.isJavaIdentifierPart(text.charAt(i + kw.length()))) {
                // not begining 
                i = text.lastIndexOf(kw, i - 1);
                continue;
            }
        }
        // Move reduced model to walker's location 
        _reduced.move(i - reducedPos);
        // reduced model points to i 
        reducedPos = i;
        // reduced model points to reducedPos 
        // Check if matching keyword should be ignored because it is within a comment, or quotes 
        ReducedModelState state = _reduced.getStateAtCurrent();
        if (!state.equals(FREE) || _isStartOfComment(text, i) || ((i > 0) && _isStartOfComment(text, i - 1))) {
            i = text.lastIndexOf(kw, reducedPos - 1);
            continue;
        } else
            break;
    }
    // end synchronized/ 
    _reduced.move(origLocation - reducedPos);
    // Restore the state of the reduced model; 
    //    } 
    if (i == -1)
        reducedPos = -1;
    // No matching keyword was found 
    return reducedPos;
}
