// endContentModel()  
//  
// Private methods  
//  
/**
     * Normalize the attribute value of a non CDATA default attribute
     * collapsing sequences of space characters (x20)
     *
     * @param value The value to normalize
     * @return Whether the value was changed or not.
     */
private boolean normalizeDefaultAttrValue(XMLString value) {
    boolean skipSpace = true;
    // skip leading spaces  
    int current = value.offset;
    int end = value.offset + value.length;
    for (int i = value.offset; i < end; i++) {
        if (value.ch[i] == ' ') {
            if (!skipSpace) {
                // take the first whitespace as a space and skip the others  
                value.ch[current++] = ' ';
                skipSpace = true;
            } else {
            }
        } else {
            // simply shift non space chars if needed  
            if (current != i) {
                value.ch[current] = value.ch[i];
            }
            current++;
            skipSpace = false;
        }
    }
    if (current != end) {
        if (skipSpace) {
            // if we finished on a space trim it  
            current--;
        }
        // set the new value length  
        value.length = current - value.offset;
        return true;
    }
    return false;
}
