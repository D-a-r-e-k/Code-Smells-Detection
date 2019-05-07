/**
         * Reads the next characters WITHOUT impacting the buffer content
         * up to current offset.
         * @param len the number of characters to read
         * @return the read string (length may be smaller if EOF is encountered)
         */
protected String nextContent(int len) throws IOException {
    final int originalOffset = fCurrentEntity.offset;
    final int originalColumnNumber = fCurrentEntity.getColumnNumber();
    final int originalCharacterOffset = fCurrentEntity.getCharacterOffset();
    char[] buff = new char[len];
    int nbRead = 0;
    for (nbRead = 0; nbRead < len; ++nbRead) {
        // read() should not clear the buffer 
        if (fCurrentEntity.offset == fCurrentEntity.length) {
            if (fCurrentEntity.length == fCurrentEntity.buffer.length) {
                fCurrentEntity.load(fCurrentEntity.buffer.length);
            } else {
                // everything was already loaded 
                break;
            }
        }
        int c = fCurrentEntity.read();
        if (c == -1) {
            break;
        } else {
            buff[nbRead] = (char) c;
        }
    }
    fCurrentEntity.restorePosition(originalOffset, originalColumnNumber, originalCharacterOffset);
    return new String(buff, 0, nbRead);
}
