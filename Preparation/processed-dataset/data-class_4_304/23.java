// scanContent():int  
/** 
     * Scans a CDATA section. 
     * <p>
     * <strong>Note:</strong> This method uses the fTempString and
     * fStringBuffer variables.
     *
     * @param complete True if the CDATA section is to be scanned
     *                 completely.
     *
     * @return True if CDATA is completely scanned.
     */
protected boolean scanCDATASection(boolean complete) throws IOException, XNIException {
    // call handler  
    if (fDocumentHandler != null) {
        fDocumentHandler.startCDATA(null);
    }
    while (true) {
        fStringBuffer.clear();
        if (!fEntityScanner.scanData("]]", fStringBuffer)) {
            if (fDocumentHandler != null && fStringBuffer.length > 0) {
                fDocumentHandler.characters(fStringBuffer, null);
            }
            int brackets = 0;
            while (fEntityScanner.skipChar(']')) {
                brackets++;
            }
            if (fDocumentHandler != null && brackets > 0) {
                fStringBuffer.clear();
                if (brackets > XMLEntityManager.DEFAULT_BUFFER_SIZE) {
                    // Handle large sequences of ']'  
                    int chunks = brackets / XMLEntityManager.DEFAULT_BUFFER_SIZE;
                    int remainder = brackets % XMLEntityManager.DEFAULT_BUFFER_SIZE;
                    for (int i = 0; i < XMLEntityManager.DEFAULT_BUFFER_SIZE; i++) {
                        fStringBuffer.append(']');
                    }
                    for (int i = 0; i < chunks; i++) {
                        fDocumentHandler.characters(fStringBuffer, null);
                    }
                    if (remainder != 0) {
                        fStringBuffer.length = remainder;
                        fDocumentHandler.characters(fStringBuffer, null);
                    }
                } else {
                    for (int i = 0; i < brackets; i++) {
                        fStringBuffer.append(']');
                    }
                    fDocumentHandler.characters(fStringBuffer, null);
                }
            }
            if (fEntityScanner.skipChar('>')) {
                break;
            }
            if (fDocumentHandler != null) {
                fStringBuffer.clear();
                fStringBuffer.append("]]");
                fDocumentHandler.characters(fStringBuffer, null);
            }
        } else {
            if (fDocumentHandler != null) {
                fDocumentHandler.characters(fStringBuffer, null);
            }
            int c = fEntityScanner.peekChar();
            if (c != -1 && isInvalidLiteral(c)) {
                if (XMLChar.isHighSurrogate(c)) {
                    fStringBuffer.clear();
                    scanSurrogates(fStringBuffer);
                    if (fDocumentHandler != null) {
                        fDocumentHandler.characters(fStringBuffer, null);
                    }
                } else {
                    reportFatalError("InvalidCharInCDSect", new Object[] { Integer.toString(c, 16) });
                    fEntityScanner.scanChar();
                }
            }
        }
    }
    fMarkupDepth--;
    // call handler  
    if (fDocumentHandler != null) {
        fDocumentHandler.endCDATA(null);
    }
    return true;
}
