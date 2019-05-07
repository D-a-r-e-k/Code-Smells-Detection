private void scanScriptContent() throws IOException {
    final XMLStringBuffer buffer = new XMLStringBuffer();
    boolean waitForEndComment = false;
    while (true) {
        int c = fCurrentEntity.read();
        if (c == -1) {
            break;
        } else if (c == '-' && endsWith(buffer, "<!-")) {
            waitForEndComment = endCommentAvailable();
        } else if (!waitForEndComment && c == '<') {
            final String next = nextContent(8) + " ";
            if (next.length() >= 8 && "/script".equalsIgnoreCase(next.substring(0, 7)) && ('>' == next.charAt(7) || Character.isWhitespace(next.charAt(7)))) {
                fCurrentEntity.rewind();
                break;
            }
        } else if (c == '>' && endsWith(buffer, "--")) {
            waitForEndComment = false;
        }
        if (c == '\r' || c == '\n') {
            fCurrentEntity.rewind();
            int newlines = skipNewlines();
            for (int i = 0; i < newlines; i++) {
                buffer.append('\n');
            }
        } else {
            buffer.append((char) c);
        }
    }
    if (fScriptStripCommentDelims) {
        reduceToContent(buffer, "<!--", "-->");
    }
    if (fScriptStripCDATADelims) {
        reduceToContent(buffer, "<![CDATA[", "]]>");
    }
    if (buffer.length > 0 && fDocumentHandler != null && fElementCount >= fElementDepth) {
        if (DEBUG_CALLBACKS) {
            System.out.println("characters(" + buffer + ")");
        }
        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = fCurrentEntity.getColumnNumber();
        fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
        fDocumentHandler.characters(buffer, locationAugs());
    }
}
