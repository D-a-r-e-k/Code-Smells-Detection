// scanXMLDeclOrTextDecl(boolean)  
/**
     * Scans a pseudo attribute.
     *
     * @param scanningTextDecl True if scanning this pseudo-attribute for a
     *                         TextDecl; false if scanning XMLDecl. This 
     *                         flag is needed to report the correct type of
     *                         error.
     * @param value            The string to fill in with the attribute 
     *                         value.
     *
     * @return The name of the attribute
     *
     * <strong>Note:</strong> This method uses fStringBuffer2, anything in it
     * at the time of calling is lost.
     */
public String scanPseudoAttribute(boolean scanningTextDecl, XMLString value) throws IOException, XNIException {
    // REVISIT: This method is used for generic scanning of   
    // pseudo attributes, but since there are only three such  
    // attributes: version, encoding, and standalone there are  
    // for performant ways of scanning them. Every decl must  
    // have a version, and in TextDecls this version must  
    // be followed by an encoding declaration. Also the  
    // methods we invoke on the scanners allow non-ASCII  
    // characters to be parsed in the decls, but since  
    // we don't even know what the actual encoding of the  
    // document is until we scan the encoding declaration  
    // you cannot reliably read any characters outside  
    // of the ASCII range here. -- mrglavas  
    String name = fEntityScanner.scanName();
    XMLEntityManager.print(fEntityManager.getCurrentEntity());
    if (name == null) {
        reportFatalError("PseudoAttrNameExpected", null);
    }
    fEntityScanner.skipDeclSpaces();
    if (!fEntityScanner.skipChar('=')) {
        reportFatalError(scanningTextDecl ? "EqRequiredInTextDecl" : "EqRequiredInXMLDecl", new Object[] { name });
    }
    fEntityScanner.skipDeclSpaces();
    int quote = fEntityScanner.peekChar();
    if (quote != '\'' && quote != '"') {
        reportFatalError(scanningTextDecl ? "QuoteRequiredInTextDecl" : "QuoteRequiredInXMLDecl", new Object[] { name });
    }
    fEntityScanner.scanChar();
    int c = fEntityScanner.scanLiteral(quote, value);
    if (c != quote) {
        fStringBuffer2.clear();
        do {
            fStringBuffer2.append(value);
            if (c != -1) {
                if (c == '&' || c == '%' || c == '<' || c == ']') {
                    fStringBuffer2.append((char) fEntityScanner.scanChar());
                } else if (XMLChar.isHighSurrogate(c)) {
                    scanSurrogates(fStringBuffer2);
                } else if (isInvalidLiteral(c)) {
                    String key = scanningTextDecl ? "InvalidCharInTextDecl" : "InvalidCharInXMLDecl";
                    reportFatalError(key, new Object[] { Integer.toString(c, 16) });
                    fEntityScanner.scanChar();
                }
            }
            c = fEntityScanner.scanLiteral(quote, value);
        } while (c != quote);
        fStringBuffer2.append(value);
        value.setValues(fStringBuffer2);
    }
    if (!fEntityScanner.skipChar(quote)) {
        reportFatalError(scanningTextDecl ? "CloseQuoteMissingInTextDecl" : "CloseQuoteMissingInXMLDecl", new Object[] { name });
    }
    // return  
    return name;
}
