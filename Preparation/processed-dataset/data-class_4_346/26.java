/**
     * Handle one of the default entities, return false if it
     * is not a default entity.
     *
     * @param ch character to be escaped.
     * @param i index into character array.
     * @param chars non-null reference to character array.
     * @param len length of chars.
     * @param fromTextNode true if the characters being processed
     * are from a text node, false if they are from an attribute value
     * @param escLF true if the linefeed should be escaped.
     *
     * @return i+1 if the character was written, else i.
     *
     * @throws java.io.IOException
     */
int accumDefaultEntity(java.io.Writer writer, char ch, int i, char[] chars, int len, boolean fromTextNode, boolean escLF) throws IOException {
    if (!escLF && CharInfo.S_LINEFEED == ch) {
        writer.write(m_lineSep, 0, m_lineSepLen);
    } else {
        // if this is text node character and a special one of those,  
        // or if this is a character from attribute value and a special one of those  
        if ((fromTextNode && m_charInfo.shouldMapTextChar(ch)) || (!fromTextNode && m_charInfo.shouldMapAttrChar(ch))) {
            String outputStringForChar = m_charInfo.getOutputStringForChar(ch);
            if (null != outputStringForChar) {
                writer.write(outputStringForChar);
            } else
                return i;
        } else
            return i;
    }
    return i + 1;
}
