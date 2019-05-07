// scanEntityRef(XMLStringBuffer,boolean):int 
/** Returns true if the specified text is present and is skipped. */
protected boolean skip(String s, boolean caseSensitive) throws IOException {
    int length = s != null ? s.length() : 0;
    for (int i = 0; i < length; i++) {
        if (fCurrentEntity.offset == fCurrentEntity.length) {
            System.arraycopy(fCurrentEntity.buffer, fCurrentEntity.offset - i, fCurrentEntity.buffer, 0, i);
            if (fCurrentEntity.load(i) == -1) {
                fCurrentEntity.offset = 0;
                return false;
            }
        }
        char c0 = s.charAt(i);
        char c1 = fCurrentEntity.getNextChar();
        if (!caseSensitive) {
            c0 = Character.toUpperCase(c0);
            c1 = Character.toUpperCase(c1);
        }
        if (c0 != c1) {
            fCurrentEntity.rewind(i + 1);
            return false;
        }
    }
    return true;
}
