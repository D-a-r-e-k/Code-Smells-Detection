// endEntity(String)  
/**
     * Scans a character reference and append the corresponding chars to the
     * specified buffer.
     *
     * <p>
     * <pre>
     * [66] CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'
     * </pre>
     *
     * <strong>Note:</strong> This method uses fStringBuffer, anything in it
     * at the time of calling is lost.
     *
     * @param buf the character buffer to append chars to
     * @param buf2 the character buffer to append non-normalized chars to
     *
     * @return the character value or (-1) on conversion failure
     */
protected int scanCharReferenceValue(XMLStringBuffer buf, XMLStringBuffer buf2) throws IOException, XNIException {
    // scan hexadecimal value  
    boolean hex = false;
    if (fEntityScanner.skipChar('x')) {
        if (buf2 != null) {
            buf2.append('x');
        }
        hex = true;
        fStringBuffer3.clear();
        boolean digit = true;
        int c = fEntityScanner.peekChar();
        digit = (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
        if (digit) {
            if (buf2 != null) {
                buf2.append((char) c);
            }
            fEntityScanner.scanChar();
            fStringBuffer3.append((char) c);
            do {
                c = fEntityScanner.peekChar();
                digit = (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
                if (digit) {
                    if (buf2 != null) {
                        buf2.append((char) c);
                    }
                    fEntityScanner.scanChar();
                    fStringBuffer3.append((char) c);
                }
            } while (digit);
        } else {
            reportFatalError("HexdigitRequiredInCharRef", null);
        }
    } else {
        fStringBuffer3.clear();
        boolean digit = true;
        int c = fEntityScanner.peekChar();
        digit = c >= '0' && c <= '9';
        if (digit) {
            if (buf2 != null) {
                buf2.append((char) c);
            }
            fEntityScanner.scanChar();
            fStringBuffer3.append((char) c);
            do {
                c = fEntityScanner.peekChar();
                digit = c >= '0' && c <= '9';
                if (digit) {
                    if (buf2 != null) {
                        buf2.append((char) c);
                    }
                    fEntityScanner.scanChar();
                    fStringBuffer3.append((char) c);
                }
            } while (digit);
        } else {
            reportFatalError("DigitRequiredInCharRef", null);
        }
    }
    // end  
    if (!fEntityScanner.skipChar(';')) {
        reportFatalError("SemicolonRequiredInCharRef", null);
    }
    if (buf2 != null) {
        buf2.append(';');
    }
    // convert string to number  
    int value = -1;
    try {
        value = Integer.parseInt(fStringBuffer3.toString(), hex ? 16 : 10);
        // character reference must be a valid XML character  
        if (isInvalid(value)) {
            StringBuffer errorBuf = new StringBuffer(fStringBuffer3.length + 1);
            if (hex)
                errorBuf.append('x');
            errorBuf.append(fStringBuffer3.ch, fStringBuffer3.offset, fStringBuffer3.length);
            reportFatalError("InvalidCharRef", new Object[] { errorBuf.toString() });
        }
    } catch (NumberFormatException e) {
        // Conversion failed, let -1 value drop through.  
        // If we end up here, the character reference was invalid.  
        StringBuffer errorBuf = new StringBuffer(fStringBuffer3.length + 1);
        if (hex)
            errorBuf.append('x');
        errorBuf.append(fStringBuffer3.ch, fStringBuffer3.offset, fStringBuffer3.length);
        reportFatalError("InvalidCharRef", new Object[] { errorBuf.toString() });
    }
    // append corresponding chars to the given buffer  
    if (!XMLChar.isSupplemental(value)) {
        buf.append((char) value);
    } else {
        // character is supplemental, split it into surrogate chars  
        buf.append(XMLChar.highSurrogate(value));
        buf.append(XMLChar.lowSurrogate(value));
    }
    // char refs notification code  
    if (fNotifyCharRefs && value != -1) {
        String literal = "#" + (hex ? "x" : "") + fStringBuffer3.toString();
        if (!fScanningAttribute) {
            fCharRefLiteral = literal;
        }
    }
    return value;
}
