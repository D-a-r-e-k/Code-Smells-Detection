private void normalizeWhitespace(String value, boolean collapse) {
    boolean skipSpace = collapse;
    char c;
    int size = value.length();
    // ensure the ch array is big enough  
    if (fNormalizedStr.ch == null || fNormalizedStr.ch.length < size) {
        fNormalizedStr.ch = new char[size];
    }
    fNormalizedStr.offset = 0;
    fNormalizedStr.length = 0;
    for (int i = 0; i < size; i++) {
        c = value.charAt(i);
        if (XMLChar.isSpace(c)) {
            if (!skipSpace) {
                // take the first whitespace as a space and skip the others  
                fNormalizedStr.ch[fNormalizedStr.length++] = ' ';
                skipSpace = collapse;
            }
        } else {
            fNormalizedStr.ch[fNormalizedStr.length++] = c;
            skipSpace = false;
        }
    }
    if (skipSpace) {
        if (fNormalizedStr.length != 0)
            // if we finished on a space trim it but also record it  
            fNormalizedStr.length--;
    }
}
