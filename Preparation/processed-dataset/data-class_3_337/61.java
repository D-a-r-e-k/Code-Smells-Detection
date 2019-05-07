/** Returns true if one of the words 'class', 'interface' or 'enum' is found
    * in non-comment text. */
public boolean containsClassOrInterfaceOrEnum() throws BadLocationException {
    /* */
    assert Utilities.TEST_MODE || EventQueue.isDispatchThread();
    int i, j;
    int reducedPos = 0;
    final String text = getText();
    final int origLocation = _currentLocation;
    try {
        // Move reduced model to beginning of file 
        _reduced.move(-origLocation);
        // Walk forward from specificed position 
        i = text.indexOf("class", reducedPos);
        j = text.indexOf("interface", reducedPos);
        if (i == -1)
            i = j;
        else if (j >= 0)
            i = Math.min(i, j);
        j = text.indexOf("enum", reducedPos);
        if (i == -1)
            i = j;
        else if (j >= 0)
            i = Math.min(i, j);
        while (i > -1) {
            // Move reduced model to walker's location 
            _reduced.move(i - reducedPos);
            // reduced model points to i 
            reducedPos = i;
            // reduced model points to reducedPos 
            // Check if matching keyword should be ignored because it is within a comment, or quotes 
            ReducedModelState state = _reduced.getStateAtCurrent();
            if (!state.equals(FREE) || _isStartOfComment(text, i) || ((i > 0) && _isStartOfComment(text, i - 1))) {
                i = text.indexOf("class", reducedPos + 1);
                j = text.indexOf("interface", reducedPos + 1);
                if (i == -1)
                    i = j;
                else if (j >= 0)
                    i = Math.min(i, j);
                j = text.indexOf("enum", reducedPos + 1);
                if (i == -1)
                    i = j;
                else if (j >= 0)
                    i = Math.min(i, j);
                continue;
            } else {
                return true;
            }
        }
        return false;
    } finally {
        _reduced.move(origLocation - reducedPos);
    }
}
