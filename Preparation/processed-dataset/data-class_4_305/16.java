/**
     * Checks whether this string would be unchanged by normalization.
     * 
     * @return -1 if the value would be unchanged by normalization,
     * otherwise the index of the first whitespace character which
     * would be transformed.
     */
protected int isUnchangedByNormalization(XMLString value) {
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
            return i - value.offset;
        }
    }
    return -1;
}
