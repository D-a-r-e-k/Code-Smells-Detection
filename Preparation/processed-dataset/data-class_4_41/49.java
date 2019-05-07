// readPreservingBufferContent():int 
/**
     * Indicates if the end comment --> is available, loading further data if needed, without to reset the buffer
     */
private boolean endCommentAvailable() throws IOException {
    int nbCaret = 0;
    final int originalOffset = fCurrentEntity.offset;
    final int originalColumnNumber = fCurrentEntity.getColumnNumber();
    final int originalCharacterOffset = fCurrentEntity.getCharacterOffset();
    while (true) {
        int c = readPreservingBufferContent();
        if (c == -1) {
            fCurrentEntity.restorePosition(originalOffset, originalColumnNumber, originalCharacterOffset);
            return false;
        } else if (c == '>' && nbCaret >= 2) {
            fCurrentEntity.restorePosition(originalOffset, originalColumnNumber, originalCharacterOffset);
            return true;
        } else if (c == '-') {
            nbCaret++;
        } else {
            nbCaret = 0;
        }
    }
}
