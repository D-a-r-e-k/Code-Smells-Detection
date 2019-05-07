// scanPI()  
/**
     * Scans a processing data. This is needed to handle the situation
     * where a document starts with a processing instruction whose 
     * target name <em>starts with</em> "xml". (e.g. xmlfoo)
     *
     * <strong>Note:</strong> This method uses fStringBuffer, anything in it
     * at the time of calling is lost.
     *
     * @param target The PI target
     * @param data The string to fill in with the data
     */
protected void scanPIData(String target, XMLString data) throws IOException, XNIException {
    // check target  
    if (target.length() == 3) {
        char c0 = Character.toLowerCase(target.charAt(0));
        char c1 = Character.toLowerCase(target.charAt(1));
        char c2 = Character.toLowerCase(target.charAt(2));
        if (c0 == 'x' && c1 == 'm' && c2 == 'l') {
            reportFatalError("ReservedPITarget", null);
        }
    }
    // spaces  
    if (!fEntityScanner.skipSpaces()) {
        if (fEntityScanner.skipString("?>")) {
            // we found the end, there is no data  
            data.clear();
            return;
        } else {
            if (fNamespaces && fEntityScanner.peekChar() == ':') {
                fEntityScanner.scanChar();
                XMLStringBuffer colonName = new XMLStringBuffer(target);
                colonName.append(":");
                String str = fEntityScanner.scanName();
                if (str != null)
                    colonName.append(str);
                reportFatalError("ColonNotLegalWithNS", new Object[] { colonName.toString() });
                fEntityScanner.skipSpaces();
            } else {
                // if there is data there should be some space  
                reportFatalError("SpaceRequiredInPI", null);
            }
        }
    }
    fStringBuffer.clear();
    // data  
    if (fEntityScanner.scanData("?>", fStringBuffer)) {
        do {
            int c = fEntityScanner.peekChar();
            if (c != -1) {
                if (XMLChar.isHighSurrogate(c)) {
                    scanSurrogates(fStringBuffer);
                } else if (isInvalidLiteral(c)) {
                    reportFatalError("InvalidCharInPI", new Object[] { Integer.toHexString(c) });
                    fEntityScanner.scanChar();
                }
            }
        } while (fEntityScanner.scanData("?>", fStringBuffer));
    }
    data.setValues(fStringBuffer);
}
