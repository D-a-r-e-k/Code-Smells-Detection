/**
     * Normalize whitespace in an XMLString converting all whitespace
     * characters to space characters.
     */
protected void normalizeWhitespace(XMLString value) {
    int end = value.offset + value.length;
    for (int i = value.offset; i < end; ++i) {
        int c = value.ch[i];
        // Performance: For XML 1.0 documents take advantage of   
        // the fact that the only legal characters below 0x20   
        // are 0x09 (TAB), 0x0A (LF) and 0x0D (CR). Since we've   
        // already determined the well-formedness of these  
        // characters it is sufficient (and safe) to check  
        // against 0x20. -- mrglavas  
        if (c < 0x20) {
            value.ch[i] = ' ';
        }
    }
}
