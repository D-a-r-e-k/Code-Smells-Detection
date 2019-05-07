// scanLiteral():String 
/** Scans a name. */
protected String scanName() throws IOException {
    fCurrentEntity.debugBufferIfNeeded("(scanName: ");
    if (fCurrentEntity.offset == fCurrentEntity.length) {
        if (fCurrentEntity.load(0) == -1) {
            fCurrentEntity.debugBufferIfNeeded(")scanName: ");
            return null;
        }
    }
    int offset = fCurrentEntity.offset;
    while (true) {
        while (fCurrentEntity.hasNext()) {
            char c = fCurrentEntity.getNextChar();
            if (!Character.isLetterOrDigit(c) && !(c == '-' || c == '.' || c == ':' || c == '_')) {
                fCurrentEntity.rewind();
                break;
            }
        }
        if (fCurrentEntity.offset == fCurrentEntity.length) {
            int length = fCurrentEntity.length - offset;
            System.arraycopy(fCurrentEntity.buffer, offset, fCurrentEntity.buffer, 0, length);
            int count = fCurrentEntity.load(length);
            offset = 0;
            if (count == -1) {
                break;
            }
        } else {
            break;
        }
    }
    int length = fCurrentEntity.offset - offset;
    String name = length > 0 ? new String(fCurrentEntity.buffer, offset, length) : null;
    fCurrentEntity.debugBufferIfNeeded(")scanName: ", " -> \"" + name + '"');
    return name;
}
