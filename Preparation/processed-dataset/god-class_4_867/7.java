// scanCDATA() 
/** Scans a comment. */
protected void scanComment() throws IOException {
    fCurrentEntity.debugBufferIfNeeded("(scanComment: ");
    fEndLineNumber = fCurrentEntity.getLineNumber();
    fEndColumnNumber = fCurrentEntity.getColumnNumber();
    fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
    XMLStringBuffer buffer = new XMLStringBuffer();
    boolean eof = scanMarkupContent(buffer, '-');
    // no --> found, comment with end only with > 
    if (eof) {
        fCurrentEntity.resetBuffer(buffer, fEndLineNumber, fEndColumnNumber, fEndCharacterOffset);
        buffer = new XMLStringBuffer();
        // take a new one to avoid interactions 
        while (true) {
            int c = fCurrentEntity.read();
            if (c == -1) {
                if (fReportErrors) {
                    fErrorReporter.reportError("HTML1007", null);
                }
                eof = true;
                break;
            } else if (c != '>') {
                buffer.append((char) c);
                continue;
            } else if (c == '\n' || c == '\r') {
                fCurrentEntity.rewind();
                int newlines = skipNewlines();
                for (int i = 0; i < newlines; i++) {
                    buffer.append('\n');
                }
                continue;
            }
            eof = false;
            break;
        }
    }
    if (fDocumentHandler != null && fElementCount >= fElementDepth) {
        if (DEBUG_CALLBACKS) {
            System.out.println("comment(" + buffer + ")");
        }
        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = fCurrentEntity.getColumnNumber();
        fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
        fDocumentHandler.comment(buffer, locationAugs());
    }
    fCurrentEntity.debugBufferIfNeeded(")scanComment: ");
    if (eof) {
        throw new EOFException();
    }
}
