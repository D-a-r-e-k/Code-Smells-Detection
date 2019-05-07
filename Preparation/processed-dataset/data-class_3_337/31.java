/** Assumes that read lock is already held. */
private int _findNextOpenCurly(String text, int pos) throws BadLocationException {
    /* */
    assert Utilities.TEST_MODE || EventQueue.isDispatchThread();
    int i;
    int reducedPos = pos;
    //    synchronized(_reduced) { 
    final int origLocation = _currentLocation;
    // Move reduced model to location pos 
    _reduced.move(pos - origLocation);
    // reduced model points to pos == reducedPos 
    // Walk forward from specificed position 
    i = text.indexOf('{', reducedPos);
    while (i > -1) {
        // Move reduced model to walker's location 
        _reduced.move(i - reducedPos);
        // reduced model points to i 
        reducedPos = i;
        // reduced model points to reducedPos 
        // Check if matching keyword should be ignored because it is within a comment, or quotes 
        ReducedModelState state = _reduced.getStateAtCurrent();
        if (!state.equals(FREE) || _isStartOfComment(text, i) || ((i > 0) && _isStartOfComment(text, i - 1))) {
            i = text.indexOf('{', reducedPos + 1);
            continue;
        } else {
            break;
        }
    }
    _reduced.move(origLocation - reducedPos);
    // Restore the state of the reduced model; 
    //    } // end synchronized 
    if (i == -1)
        reducedPos = -1;
    // No matching brace was found 
    return reducedPos;
}
