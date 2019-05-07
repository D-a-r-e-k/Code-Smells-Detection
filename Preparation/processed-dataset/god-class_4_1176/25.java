/**
     * Once a surrogate has been detected, write out the pair of
     * characters if it is in the encoding, or if there is no
     * encoding, otherwise write out an entity reference
     * of the value of the unicode code point of the character
     * represented by the high/low surrogate pair.
     * <p>
     * An exception is thrown if there is no low surrogate in the pair,
     * because the array ends unexpectely, or if the low char is there
     * but its value is such that it is not a low surrogate.
     *
     * @param c the first (high) part of the surrogate, which
     * must be confirmed before calling this method.
     * @param ch Character array.
     * @param i position Where the surrogate was detected.
     * @param end The end index of the significant characters.
     * @return 0 if the pair of characters was written out as-is,
     * the unicode code point of the character represented by
     * the surrogate pair if an entity reference with that value
     * was written out. 
     * 
     * @throws IOException
     * @throws org.xml.sax.SAXException if invalid UTF-16 surrogate detected.
     */
protected int writeUTF16Surrogate(char c, char ch[], int i, int end) throws IOException {
    int codePoint = 0;
    if (i + 1 >= end) {
        throw new IOException(Utils.messages.createMessage(MsgKey.ER_INVALID_UTF16_SURROGATE, new Object[] { Integer.toHexString((int) c) }));
    }
    final char high = c;
    final char low = ch[i + 1];
    if (!Encodings.isLowUTF16Surrogate(low)) {
        throw new IOException(Utils.messages.createMessage(MsgKey.ER_INVALID_UTF16_SURROGATE, new Object[] { Integer.toHexString((int) c) + " " + Integer.toHexString(low) }));
    }
    final java.io.Writer writer = m_writer;
    // If we make it to here we have a valid high, low surrogate pair  
    if (m_encodingInfo.isInEncoding(c, low)) {
        // If the character formed by the surrogate pair  
        // is in the encoding, so just write it out  
        writer.write(ch, i, 2);
    } else {
        // Don't know what to do with this char, it is  
        // not in the encoding and not a high char in  
        // a surrogate pair, so write out as an entity ref  
        final String encoding = getEncoding();
        if (encoding != null) {
            /* The output encoding is known, 
                 * so somthing is wrong.
                  */
            codePoint = Encodings.toCodePoint(high, low);
            // not in the encoding, so write out a character reference  
            writer.write('&');
            writer.write('#');
            writer.write(Integer.toString(codePoint));
            writer.write(';');
        } else {
            /* The output encoding is not known,
                 * so just write it out as-is.
                 */
            writer.write(ch, i, 2);
        }
    }
    // non-zero only if character reference was written out.  
    return codePoint;
}
