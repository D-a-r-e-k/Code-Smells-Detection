/** Return the current column of the cursor position. Uses a 0 based index. */
public int getCurrentCol() {
    Element root = getDefaultRootElement();
    int line = root.getElementIndex(_currentLocation);
    return _currentLocation - root.getElement(line).getStartOffset();
}
