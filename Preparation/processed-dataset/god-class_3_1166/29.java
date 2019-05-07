/** Uncomments a single line.  This simply looks for a leading "//".  Assumes that cursor is already located at the
    * beginning of line.  Also assumes that write lock and _reduced lock are already held.
    */
private int _uncommentLine() throws BadLocationException {
    // Look for "//" at the beginning of the line, and remove it. 
    //    Utilities.show("Uncomment line at location " + _currentLocation); 
    //    Utilities.show("Preceding char = '" + getText().charAt(_currentLocation - 1) + "'"); 
    //    Utilities.show("Line = \n" + getText(_currentLocation, getLineEndPos(_currentLocation) - _currentLocation + 1)); 
    int pos1 = getText().indexOf("//", _currentLocation);
    // TODO: get text of current line instead of whole document 
    if (pos1 < 0)
        return NO_COMMENT_OFFSET;
    int pos2 = getFirstNonWSCharPos(_currentLocation, true);
    //    Utilities.show("Pos1 = " + pos1 + " Pos2 = " + pos2); 
    if (pos1 != pos2)
        return NO_COMMENT_OFFSET;
    remove(pos1, 2);
    return WING_COMMENT_OFFSET;
}
