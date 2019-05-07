/** Reads a single character, preserving the old buffer content */
protected int readPreservingBufferContent() throws IOException {
    fCurrentEntity.debugBufferIfNeeded("(read: ");
    if (fCurrentEntity.offset == fCurrentEntity.length) {
        if (fCurrentEntity.load(fCurrentEntity.length) < 1) {
            if (DEBUG_BUFFER) {
                System.out.println(")read: -> -1");
            }
            return -1;
        }
    }
    final char c = fCurrentEntity.getNextChar();
    fCurrentEntity.debugBufferIfNeeded(")read: ", " -> " + c);
    return c;
}
