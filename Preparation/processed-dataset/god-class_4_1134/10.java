// scanPIData(String,XMLString)  
/**
     * Scans a comment.
     * <p>
     * <pre>
     * [15] Comment ::= '&lt!--' ((Char - '-') | ('-' (Char - '-')))* '-->'
     * </pre>
     * <p>
     * <strong>Note:</strong> Called after scanning past '&lt;!--'
     * <strong>Note:</strong> This method uses fString, anything in it
     * at the time of calling is lost.
     *
     * @param text The buffer to fill in with the text.
     */
protected void scanComment(XMLStringBuffer text) throws IOException, XNIException {
    // text  
    // REVISIT: handle invalid character, eof  
    text.clear();
    while (fEntityScanner.scanData("--", text)) {
        int c = fEntityScanner.peekChar();
        if (c != -1) {
            if (XMLChar.isHighSurrogate(c)) {
                scanSurrogates(text);
            } else if (isInvalidLiteral(c)) {
                reportFatalError("InvalidCharInComment", new Object[] { Integer.toHexString(c) });
                fEntityScanner.scanChar();
            }
        }
    }
    if (!fEntityScanner.skipChar('>')) {
        reportFatalError("DashDashInComment", null);
    }
}
