/** Uncomments all lines between selStart and selEnd, inclusive.  The cursor position is unchanged by the operation.
    * @param selStart the document offset for the start of the selection
    * @param selEnd the document offset for the end of the selection
    */
public int uncommentLines(int selStart, int selEnd) {
    //int key = _undoManager.startCompoundEdit(); //commented out for FrenchKeyBoardFix 
    int toReturn = selEnd;
    if (selStart == selEnd) {
        try {
            setCurrentLocation(_getLineStartPos(selStart));
            _uncommentLine();
            // accesses _reduced 
            toReturn -= WING_COMMENT_OFFSET;
        } catch (BadLocationException e) {
            throw new UnexpectedException(e);
        }
    } else
        toReturn = uncommentBlock(selStart, selEnd);
    //_undoManager.endCompoundEdit(key); //Commented out for FrenchKeyBoardFix, Replaced with endLastCompoundEdit(); 
    _undoManager.endLastCompoundEdit();
    return toReturn;
}
