// scanAttributeValue()  
/**
     * Scans External ID and return the public and system IDs.
     *
     * @param identifiers An array of size 2 to return the system id,
     *                    and public id (in that order).
     * @param optionalSystemId Specifies whether the system id is optional.
     *
     * <strong>Note:</strong> This method uses fString and fStringBuffer,
     * anything in them at the time of calling is lost.
     */
protected void scanExternalID(String[] identifiers, boolean optionalSystemId) throws IOException, XNIException {
    String systemId = null;
    String publicId = null;
    if (fEntityScanner.skipString("PUBLIC")) {
        if (!fEntityScanner.skipSpaces()) {
            reportFatalError("SpaceRequiredAfterPUBLIC", null);
        }
        scanPubidLiteral(fString);
        publicId = fString.toString();
        if (!fEntityScanner.skipSpaces() && !optionalSystemId) {
            reportFatalError("SpaceRequiredBetweenPublicAndSystem", null);
        }
    }
    if (publicId != null || fEntityScanner.skipString("SYSTEM")) {
        if (publicId == null && !fEntityScanner.skipSpaces()) {
            reportFatalError("SpaceRequiredAfterSYSTEM", null);
        }
        int quote = fEntityScanner.peekChar();
        if (quote != '\'' && quote != '"') {
            if (publicId != null && optionalSystemId) {
                // looks like we don't have any system id  
                // simply return the public id  
                identifiers[0] = null;
                identifiers[1] = publicId;
                return;
            }
            reportFatalError("QuoteRequiredInSystemID", null);
        }
        fEntityScanner.scanChar();
        XMLString ident = fString;
        if (fEntityScanner.scanLiteral(quote, ident) != quote) {
            fStringBuffer.clear();
            do {
                fStringBuffer.append(ident);
                int c = fEntityScanner.peekChar();
                if (XMLChar.isMarkup(c) || c == ']') {
                    fStringBuffer.append((char) fEntityScanner.scanChar());
                } else if (XMLChar.isHighSurrogate(c)) {
                    scanSurrogates(fStringBuffer);
                } else if (isInvalidLiteral(c)) {
                    reportFatalError("InvalidCharInSystemID", new Object[] { Integer.toHexString(c) });
                    fEntityScanner.scanChar();
                }
            } while (fEntityScanner.scanLiteral(quote, ident) != quote);
            fStringBuffer.append(ident);
            ident = fStringBuffer;
        }
        systemId = ident.toString();
        if (!fEntityScanner.skipChar(quote)) {
            reportFatalError("SystemIDUnterminated", null);
        }
    }
    // store result in array  
    identifiers[0] = systemId;
    identifiers[1] = publicId;
}
