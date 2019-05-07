// note: need to update this to work with pos 
public String getNextTopLevelClassName(int startPos, int endPos) throws ClassNameNotFoundException {
    int oldPos = _currentLocation;
    try {
        setCurrentLocation(startPos);
        final int textLength = endPos - startPos;
        final String text = getText(startPos, textLength);
        int index;
        int indexOfClass = _findKeywordAtToplevel("class", text, startPos);
        int indexOfInterface = _findKeywordAtToplevel("interface", text, startPos);
        int indexOfEnum = _findKeywordAtToplevel("enum", text, startPos);
        //If class exists at top level AND either there is no interface at top level or the index of class precedes the index of the top 
        //level interface, AND the same for top level enum, then the class is the first top level declaration 
        if (indexOfClass > -1 && (indexOfInterface <= -1 || indexOfClass < indexOfInterface) && (indexOfEnum <= -1 || indexOfClass < indexOfEnum)) {
            index = indexOfClass + "class".length();
        } else if (indexOfInterface > -1 && (indexOfClass <= -1 || indexOfInterface < indexOfClass) && (indexOfEnum <= -1 || indexOfInterface < indexOfEnum)) {
            index = indexOfInterface + "interface".length();
        } else if (indexOfEnum > -1 && (indexOfClass <= -1 || indexOfEnum < indexOfClass) && (indexOfInterface <= -1 || indexOfEnum < indexOfInterface)) {
            index = indexOfEnum + "enum".length();
        } else {
            // no index was valid 
            throw ClassNameNotFoundException.DEFAULT;
        }
        // we have a valid index 
        return getNextIdentifier(startPos + index);
    } catch (BadLocationException ble) {
        throw new UnexpectedException(ble);
    } catch (IllegalStateException e) {
        throw new ClassNameNotFoundException("No top level class name found");
    } finally {
        setCurrentLocation(oldPos);
    }
}
