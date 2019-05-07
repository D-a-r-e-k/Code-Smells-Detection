/** Reads a <CODE>String</CODE> from the font file as bytes using the Cp1252
     *  encoding.
     * @param length the length of bytes to read
     * @return the <CODE>String</CODE> read
     * @throws IOException the font file could not be read
     */
protected String readStandardString(int length) throws IOException {
    byte buf[] = new byte[length];
    rf.readFully(buf);
    try {
        return new String(buf, WINANSI);
    } catch (Exception e) {
        throw new ExceptionConverter(e);
    }
}
