/** Reads a Unicode <CODE>String</CODE> from the font file. Each character is
     *  represented by two bytes.
     * @param length the length of bytes to read. The <CODE>String</CODE> will have <CODE>length</CODE>/2
     * characters
     * @return the <CODE>String</CODE> read
     * @throws IOException the font file could not be read
     */
protected String readUnicodeString(int length) throws IOException {
    StringBuffer buf = new StringBuffer();
    length /= 2;
    for (int k = 0; k < length; ++k) {
        buf.append(rf.readChar());
    }
    return buf.toString();
}
