/** Return the current line of the cursor position.  Uses a 1-based index. */
public int getCurrentLine() {
    return getLineOfOffset(_currentLocation);
}
