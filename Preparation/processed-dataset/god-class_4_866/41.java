// skipSpaces() 
/** Skips newlines and returns the number of newlines skipped. */
protected int skipNewlines() throws IOException {
    fCurrentEntity.debugBufferIfNeeded("(skipNewlines: ");
    if (!fCurrentEntity.hasNext()) {
        if (fCurrentEntity.load(0) == -1) {
            fCurrentEntity.debugBufferIfNeeded(")skipNewlines: ");
            return 0;
        }
    }
    char c = fCurrentEntity.getCurrentChar();
    int newlines = 0;
    int offset = fCurrentEntity.offset;
    if (c == '\n' || c == '\r') {
        do {
            c = fCurrentEntity.getNextChar();
            if (c == '\r') {
                newlines++;
                if (fCurrentEntity.offset == fCurrentEntity.length) {
                    offset = 0;
                    fCurrentEntity.offset = newlines;
                    if (fCurrentEntity.load(newlines) == -1) {
                        break;
                    }
                }
                if (fCurrentEntity.getCurrentChar() == '\n') {
                    fCurrentEntity.offset++;
                    fCurrentEntity.characterOffset_++;
                    offset++;
                }
            } else if (c == '\n') {
                newlines++;
                if (fCurrentEntity.offset == fCurrentEntity.length) {
                    offset = 0;
                    fCurrentEntity.offset = newlines;
                    if (fCurrentEntity.load(newlines) == -1) {
                        break;
                    }
                }
            } else {
                fCurrentEntity.rewind();
                break;
            }
        } while (fCurrentEntity.offset < fCurrentEntity.length - 1);
        fCurrentEntity.incLine(newlines);
    }
    fCurrentEntity.debugBufferIfNeeded(")skipNewlines: ", " -> " + newlines);
    return newlines;
}
