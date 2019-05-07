/**
     * Scans public ID literal.
     *
     * [12] PubidLiteral ::= '"' PubidChar* '"' | "'" (PubidChar - "'")* "'" 
     * [13] PubidChar::= #x20 | #xD | #xA | [a-zA-Z0-9] | [-'()+,./:=?;!*#@$_%]
     *
     * The returned string is normalized according to the following rule,
     * from http://www.w3.org/TR/REC-xml#dt-pubid:
     *
     * Before a match is attempted, all strings of white space in the public
     * identifier must be normalized to single space characters (#x20), and
     * leading and trailing white space must be removed.
     *
     * @param literal The string to fill in with the public ID literal.
     * @return True on success.
     *
     * <strong>Note:</strong> This method uses fStringBuffer, anything in it at
     * the time of calling is lost.
     */
protected boolean scanPubidLiteral(XMLString literal) throws IOException, XNIException {
    int quote = fEntityScanner.scanChar();
    if (quote != '\'' && quote != '"') {
        reportFatalError("QuoteRequiredInPublicID", null);
        return false;
    }
    fStringBuffer.clear();
    // skip leading whitespace  
    boolean skipSpace = true;
    boolean dataok = true;
    while (true) {
        int c = fEntityScanner.scanChar();
        if (c == ' ' || c == '\n' || c == '\r') {
            if (!skipSpace) {
                // take the first whitespace as a space and skip the others  
                fStringBuffer.append(' ');
                skipSpace = true;
            }
        } else if (c == quote) {
            if (skipSpace) {
                // if we finished on a space let's trim it  
                fStringBuffer.length--;
            }
            literal.setValues(fStringBuffer);
            break;
        } else if (XMLChar.isPubid(c)) {
            fStringBuffer.append((char) c);
            skipSpace = false;
        } else if (c == -1) {
            reportFatalError("PublicIDUnterminated", null);
            return false;
        } else {
            dataok = false;
            reportFatalError("InvalidCharInPublicID", new Object[] { Integer.toHexString(c) });
        }
    }
    return dataok;
}
