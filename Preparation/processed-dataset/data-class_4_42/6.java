// scanCharacters() 
/** Scans a CDATA section. */
protected void scanCDATA() throws IOException {
    fCurrentEntity.debugBufferIfNeeded("(scanCDATA: ");
    fStringBuffer.clear();
    if (fCDATASections) {
        if (fDocumentHandler != null && fElementCount >= fElementDepth) {
            fEndLineNumber = fCurrentEntity.getLineNumber();
            fEndColumnNumber = fCurrentEntity.getColumnNumber();
            fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
            if (DEBUG_CALLBACKS) {
                System.out.println("startCDATA()");
            }
            fDocumentHandler.startCDATA(locationAugs());
        }
    } else {
        fStringBuffer.append("[CDATA[");
    }
    boolean eof = scanMarkupContent(fStringBuffer, ']');
    if (!fCDATASections) {
        fStringBuffer.append("]]");
    }
    if (fDocumentHandler != null && fElementCount >= fElementDepth) {
        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = fCurrentEntity.getColumnNumber();
        fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
        if (fCDATASections) {
            if (DEBUG_CALLBACKS) {
                System.out.println("characters(" + fStringBuffer + ")");
            }
            fDocumentHandler.characters(fStringBuffer, locationAugs());
            if (DEBUG_CALLBACKS) {
                System.out.println("endCDATA()");
            }
            fDocumentHandler.endCDATA(locationAugs());
        } else {
            if (DEBUG_CALLBACKS) {
                System.out.println("comment(" + fStringBuffer + ")");
            }
            fDocumentHandler.comment(fStringBuffer, locationAugs());
        }
    }
    fCurrentEntity.debugBufferIfNeeded(")scanCDATA: ");
    if (eof) {
        throw new EOFException();
    }
}
