// 
// Protected methods 
// 
/** Scans characters. */
protected void scanCharacters() throws IOException {
    fCurrentEntity.debugBufferIfNeeded("(scanCharacters: ");
    fStringBuffer.clear();
    while (true) {
        int newlines = skipNewlines();
        if (newlines == 0 && fCurrentEntity.offset == fCurrentEntity.length) {
            fCurrentEntity.debugBufferIfNeeded(")scanCharacters: ");
            break;
        }
        char c;
        int offset = fCurrentEntity.offset - newlines;
        for (int i = offset; i < fCurrentEntity.offset; i++) {
            fCurrentEntity.buffer[i] = '\n';
        }
        while (fCurrentEntity.hasNext()) {
            c = fCurrentEntity.getNextChar();
            if (c == '<' || c == '&' || c == '\n' || c == '\r') {
                fCurrentEntity.rewind();
                break;
            }
        }
        if (fCurrentEntity.offset > offset && fDocumentHandler != null && fElementCount >= fElementDepth) {
            if (DEBUG_CALLBACKS) {
                final XMLString xmlString = new XMLString(fCurrentEntity.buffer, offset, fCurrentEntity.offset - offset);
                System.out.println("characters(" + xmlString + ")");
            }
            fEndLineNumber = fCurrentEntity.getLineNumber();
            fEndColumnNumber = fCurrentEntity.getColumnNumber();
            fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
            fStringBuffer.append(fCurrentEntity.buffer, offset, fCurrentEntity.offset - offset);
        }
        fCurrentEntity.debugBufferIfNeeded(")scanCharacters: ");
        boolean hasNext = fCurrentEntity.offset < fCurrentEntity.buffer.length;
        int next = hasNext ? fCurrentEntity.getCurrentChar() : -1;
        if (next == '&' || next == '<' || next == -1) {
            break;
        }
    }
    //end while 
    if (fStringBuffer.length != 0) {
        fDocumentHandler.characters(fStringBuffer, locationAugs());
    }
}
