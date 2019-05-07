/** Comments out all lines between selStart and selEnd, inclusive. The cursor position is unchanged by the operation.
    * @param selStart the document offset for the start of the selection
    * @param selEnd the document offset for the end of the selection
    */
public int commentLines(int selStart, int selEnd) {
    //int key = _undoManager.startCompoundEdit();  //Uncommented in regards to the FrenchKeyBoardFix 
    int toReturn = selEnd;
    if (selStart == selEnd) {
        setCurrentLocation(_getLineStartPos(selStart));
        //          Position oldCurrentPosition = createUnwrappedPosition(_currentLocation); 
        _commentLine();
        toReturn += WING_COMMENT_OFFSET;
    } else
        toReturn = commentBlock(selStart, selEnd);
    _undoManager.endLastCompoundEdit();
    //Changed from endCompoundEdit(key) for FrenchKeyBoardFix 
    return toReturn;
}
