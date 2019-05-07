/**
     * Process a dirty character and any preeceding clean characters
     * that were not yet processed.
     * @param chars array of characters being processed
     * @param end one (1) beyond the last character 
     * in chars to be processed
     * @param i the index of the dirty character
     * @param ch the character in chars[i]
     * @param lastDirty the last dirty character previous to i
     * @param fromTextNode true if the characters being processed are
     * from a text node, false if they are from an attribute value.
     * @return the index of the last character processed
     */
private int processDirty(char[] chars, int end, int i, char ch, int lastDirty, boolean fromTextNode) throws IOException {
    int startClean = lastDirty + 1;
    // if we have some clean characters accumulated  
    // process them before the dirty one.                     
    if (i > startClean) {
        int lengthClean = i - startClean;
        m_writer.write(chars, startClean, lengthClean);
    }
    // process the "dirty" character  
    if (CharInfo.S_LINEFEED == ch && fromTextNode) {
        m_writer.write(m_lineSep, 0, m_lineSepLen);
    } else {
        startClean = accumDefaultEscape(m_writer, (char) ch, i, chars, end, fromTextNode, false);
        i = startClean - 1;
    }
    // Return the index of the last character that we just processed   
    // which is a dirty character.  
    return i;
}
