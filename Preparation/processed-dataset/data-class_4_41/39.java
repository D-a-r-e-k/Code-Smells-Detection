// skip(String):boolean 
/** Skips markup. */
protected boolean skipMarkup(boolean balance) throws IOException {
    fCurrentEntity.debugBufferIfNeeded("(skipMarkup: ");
    int depth = 1;
    boolean slashgt = false;
    OUTER: while (true) {
        if (fCurrentEntity.offset == fCurrentEntity.length) {
            if (fCurrentEntity.load(0) == -1) {
                break OUTER;
            }
        }
        while (fCurrentEntity.hasNext()) {
            char c = fCurrentEntity.getNextChar();
            if (balance && c == '<') {
                depth++;
            } else if (c == '>') {
                depth--;
                if (depth == 0) {
                    break OUTER;
                }
            } else if (c == '/') {
                if (fCurrentEntity.offset == fCurrentEntity.length) {
                    if (fCurrentEntity.load(0) == -1) {
                        break OUTER;
                    }
                }
                c = fCurrentEntity.getNextChar();
                if (c == '>') {
                    slashgt = true;
                    depth--;
                    if (depth == 0) {
                        break OUTER;
                    }
                } else {
                    fCurrentEntity.rewind();
                }
            } else if (c == '\r' || c == '\n') {
                fCurrentEntity.rewind();
                skipNewlines();
            }
        }
    }
    fCurrentEntity.debugBufferIfNeeded(")skipMarkup: ", " -> " + slashgt);
    return slashgt;
}
