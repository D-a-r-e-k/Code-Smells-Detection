/** Removes a block of text from the specified location. We don't update the reduced model here; that happens
    * in {@link #removeUpdate}.
    */
public void remove(int offset, int len) throws BadLocationException {
    if (len == 0)
        return;
    _setModifiedSinceSave();
    super.remove(offset, len);
}
