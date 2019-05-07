// skipMarkup():boolean 
/** Skips whitespace. */
protected boolean skipSpaces() throws IOException {
    fCurrentEntity.debugBufferIfNeeded("(skipSpaces: ");
    boolean spaces = false;
    while (true) {
        if (fCurrentEntity.offset == fCurrentEntity.length) {
            if (fCurrentEntity.load(0) == -1) {
                break;
            }
        }
        char c = fCurrentEntity.getNextChar();
        if (!Character.isWhitespace(c)) {
            fCurrentEntity.rewind();
            break;
        }
        spaces = true;
        if (c == '\r' || c == '\n') {
            fCurrentEntity.rewind();
            skipNewlines();
            continue;
        }
    }
    fCurrentEntity.debugBufferIfNeeded(")skipSpaces: ", " -> " + spaces);
    return spaces;
}
