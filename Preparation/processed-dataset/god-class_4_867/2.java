// scan(boolean):boolean 
/**
         * Scans the content of <noscript>: it doesn't get parsed but is considered as plain text
         * when feature {@link HTMLScanner#PARSE_NOSCRIPT_CONTENT} is set to false.
         * @param the tag for which content is scanned (one of "noscript" or "noframes")
         * @throws IOException
         */
private void scanNoXxxContent(final String tagName) throws IOException {
    final XMLStringBuffer buffer = new XMLStringBuffer();
    final String end = "/" + tagName;
    while (true) {
        int c = fCurrentEntity.read();
        if (c == -1) {
            break;
        }
        if (c == '<') {
            final String next = nextContent(10) + " ";
            if (next.length() >= 10 && end.equalsIgnoreCase(next.substring(0, end.length())) && ('>' == next.charAt(9) || Character.isWhitespace(next.charAt(9)))) {
                fCurrentEntity.rewind();
                break;
            }
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
    if (buffer.length > 0 && fDocumentHandler != null) {
        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = fCurrentEntity.getColumnNumber();
        fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
        fDocumentHandler.characters(buffer, locationAugs());
    }
}
