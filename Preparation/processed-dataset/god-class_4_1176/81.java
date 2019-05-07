/**
     * Sets the end of line characters to be used during serialization
     * @param eolChars A character array corresponding to the characters to be used.
     */
public void setNewLine(char[] eolChars) {
    m_lineSep = eolChars;
    m_lineSepLen = eolChars.length;
}
