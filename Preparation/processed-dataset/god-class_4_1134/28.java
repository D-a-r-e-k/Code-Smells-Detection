// getVersionNotSupportedKey: String  
/**
     * Scans surrogates and append them to the specified buffer.
     * <p>
     * <strong>Note:</strong> This assumes the current char has already been
     * identified as a high surrogate.
     *
     * @param buf The StringBuffer to append the read surrogates to.
     * @return True if it succeeded.
     */
protected boolean scanSurrogates(XMLStringBuffer buf) throws IOException, XNIException {
    int high = fEntityScanner.scanChar();
    int low = fEntityScanner.peekChar();
    if (!XMLChar.isLowSurrogate(low)) {
        reportFatalError("InvalidCharInContent", new Object[] { Integer.toString(high, 16) });
        return false;
    }
    fEntityScanner.scanChar();
    // convert surrogates to supplemental character  
    int c = XMLChar.supplemental((char) high, (char) low);
    // supplemental character must be a valid XML character  
    if (isInvalid(c)) {
        reportFatalError("InvalidCharInContent", new Object[] { Integer.toString(c, 16) });
        return false;
    }
    // fill in the buffer  
    buf.append((char) high);
    buf.append((char) low);
    return true;
}
