/**
     * Escape and writer.write a character.
     *
     * @param ch character to be escaped.
     * @param i index into character array.
     * @param chars non-null reference to character array.
     * @param len length of chars.
     * @param fromTextNode true if the characters being processed are
     * from a text node, false if the characters being processed are from
     * an attribute value.
     * @param escLF true if the linefeed should be escaped.
     *
     * @return i+1 if a character was written, i+2 if two characters
     * were written out, else return i.
     *
     * @throws org.xml.sax.SAXException
     */
private int accumDefaultEscape(Writer writer, char ch, int i, char[] chars, int len, boolean fromTextNode, boolean escLF) throws IOException {
    int pos = accumDefaultEntity(writer, ch, i, chars, len, fromTextNode, escLF);
    if (i == pos) {
        if (Encodings.isHighUTF16Surrogate(ch)) {
            // Should be the UTF-16 low surrogate of the hig/low pair.  
            char next;
            // Unicode code point formed from the high/low pair.  
            int codePoint = 0;
            if (i + 1 >= len) {
                throw new IOException(Utils.messages.createMessage(MsgKey.ER_INVALID_UTF16_SURROGATE, new Object[] { Integer.toHexString(ch) }));
            } else {
                next = chars[++i];
                if (!(Encodings.isLowUTF16Surrogate(next)))
                    throw new IOException(Utils.messages.createMessage(MsgKey.ER_INVALID_UTF16_SURROGATE, new Object[] { Integer.toHexString(ch) + " " + Integer.toHexString(next) }));
                //"Invalid UTF-16 surrogate detected: "  
                //+Integer.toHexString(ch)+" "+Integer.toHexString(next));  
                codePoint = Encodings.toCodePoint(ch, next);
            }
            writer.write("&#");
            writer.write(Integer.toString(codePoint));
            writer.write(';');
            pos += 2;
        } else {
            /*  This if check is added to support control characters in XML 1.1.
                 *  If a character is a Control Character within C0 and C1 range, it is desirable
                 *  to write it out as Numeric Character Reference(NCR) regardless of XML Version
                 *  being used for output document.
                 */
            if (isCharacterInC0orC1Range(ch) || isNELorLSEPCharacter(ch)) {
                writer.write("&#");
                writer.write(Integer.toString(ch));
                writer.write(';');
            } else if ((!escapingNotNeeded(ch) || ((fromTextNode && m_charInfo.shouldMapTextChar(ch)) || (!fromTextNode && m_charInfo.shouldMapAttrChar(ch)))) && m_elemContext.m_currentElemDepth > 0) {
                writer.write("&#");
                writer.write(Integer.toString(ch));
                writer.write(';');
            } else {
                writer.write(ch);
            }
            pos++;
        }
    }
    return pos;
}
