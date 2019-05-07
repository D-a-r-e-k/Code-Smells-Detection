/** Returns the name of the class or interface enclosing the caret position at the top level.
    * @return Name of enclosing class or interface
    * @throws ClassNameNotFoundException if no enclosing class found
    */
public String getEnclosingTopLevelClassName(int pos) throws ClassNameNotFoundException {
    int oldPos = _currentLocation;
    try {
        setCurrentLocation(pos);
        BraceInfo info = _getEnclosingBrace();
        // Find top level open brace 
        int topLevelBracePos = -1;
        String braceType = info.braceType();
        while (!braceType.equals(BraceInfo.NONE)) {
            if (braceType.equals(BraceInfo.OPEN_CURLY)) {
                topLevelBracePos = _currentLocation - info.distance();
            }
            move(-info.distance());
            info = _getEnclosingBrace();
            braceType = info.braceType();
        }
        if (topLevelBracePos == -1) {
            // No top level brace was found, so we can't find a top level class name 
            setCurrentLocation(oldPos);
            throw new ClassNameNotFoundException("no top level brace found");
        }
        char[] delims = { '{', '}', ';' };
        int prevDelimPos = findPrevDelimiter(topLevelBracePos, delims);
        if (prevDelimPos == -1) {
            // Search from start of doc 
            prevDelimPos = 0;
        } else
            prevDelimPos++;
        setCurrentLocation(oldPos);
        // Parse out the class name 
        return getNextTopLevelClassName(prevDelimPos, topLevelBracePos);
    } catch (BadLocationException ble) {
        throw new UnexpectedException(ble);
    } finally {
        setCurrentLocation(oldPos);
    }
}
