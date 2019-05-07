// scanAttribute(XMLAttributes)  
/**
     * Scans element content.
     *
     * @return Returns the next character on the stream.
     */
protected int scanContent() throws IOException, XNIException {
    XMLString content = fTempString;
    int c = fEntityScanner.scanContent(content);
    if (c == '\r') {
        // happens when there is the character reference &#13;  
        fEntityScanner.scanChar();
        fStringBuffer.clear();
        fStringBuffer.append(fTempString);
        fStringBuffer.append((char) c);
        content = fStringBuffer;
        c = -1;
    }
    if (fDocumentHandler != null && content.length > 0) {
        fDocumentHandler.characters(content, null);
    }
    if (c == ']' && fTempString.length == 0) {
        fStringBuffer.clear();
        fStringBuffer.append((char) fEntityScanner.scanChar());
        // remember where we are in case we get an endEntity before we  
        // could flush the buffer out - this happens when we're parsing an  
        // entity which ends with a ]  
        fInScanContent = true;
        //  
        // We work on a single character basis to handle cases such as:  
        // ']]]>' which we might otherwise miss.  
        //  
        if (fEntityScanner.skipChar(']')) {
            fStringBuffer.append(']');
            while (fEntityScanner.skipChar(']')) {
                fStringBuffer.append(']');
            }
            if (fEntityScanner.skipChar('>')) {
                reportFatalError("CDEndInContent", null);
            }
        }
        if (fDocumentHandler != null && fStringBuffer.length != 0) {
            fDocumentHandler.characters(fStringBuffer, null);
        }
        fInScanContent = false;
        c = -1;
    }
    return c;
}
