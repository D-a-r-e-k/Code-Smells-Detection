// handleCharacters(XMLString)  
/**
     * Normalize whitespace in an XMLString according to the rules defined
     * in XML Schema specifications.
     * @param value    The string to normalize.
     * @param collapse replace or collapse
     */
private void normalizeWhitespace(XMLString value, boolean collapse) {
    boolean skipSpace = collapse;
    boolean sawNonWS = false;
    boolean leading = false;
    boolean trailing = false;
    char c;
    int size = value.offset + value.length;
    // ensure the ch array is big enough  
    if (fNormalizedStr.ch == null || fNormalizedStr.ch.length < value.length + 1) {
        fNormalizedStr.ch = new char[value.length + 1];
    }
    // don't include the leading ' ' for now. might include it later.  
    fNormalizedStr.offset = 1;
    fNormalizedStr.length = 1;
    for (int i = value.offset; i < size; i++) {
        c = value.ch[i];
        if (XMLChar.isSpace(c)) {
            if (!skipSpace) {
                // take the first whitespace as a space and skip the others  
                fNormalizedStr.ch[fNormalizedStr.length++] = ' ';
                skipSpace = collapse;
            }
            if (!sawNonWS) {
                // this is a leading whitespace, record it  
                leading = true;
            }
        } else {
            fNormalizedStr.ch[fNormalizedStr.length++] = c;
            skipSpace = false;
            sawNonWS = true;
        }
    }
    if (skipSpace) {
        if (fNormalizedStr.length > 1) {
            // if we finished on a space trim it but also record it  
            fNormalizedStr.length--;
            trailing = true;
        } else if (leading && !fFirstChunk) {
            // if all we had was whitespace we skipped record it as  
            // trailing whitespace as well  
            trailing = true;
        }
    }
    if (fNormalizedStr.length > 1) {
        if (!fFirstChunk && (fWhiteSpace == XSSimpleType.WS_COLLAPSE)) {
            if (fTrailing) {
                // previous chunk ended on whitespace  
                // insert whitespace  
                fNormalizedStr.offset = 0;
                fNormalizedStr.ch[0] = ' ';
            } else if (leading) {
                // previous chunk ended on character,  
                // this chunk starts with whitespace  
                fNormalizedStr.offset = 0;
                fNormalizedStr.ch[0] = ' ';
            }
        }
    }
    // The length includes the leading ' '. Now removing it.  
    fNormalizedStr.length -= fNormalizedStr.offset;
    fTrailing = trailing;
    if (trailing || sawNonWS)
        fFirstChunk = false;
}
