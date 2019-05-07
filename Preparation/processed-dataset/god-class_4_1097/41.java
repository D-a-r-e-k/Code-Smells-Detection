/**
     * Retrieves an enhanced coordinate from the specified tokenizer.
     * An enhanced coordinate can be a double, or a '?' followed by a
     * formula name, or a '$' followed by an index to a modifier.
     */
private Object nextEnhancedCoordinate(StreamPosTokenizer tt, String str) throws IOException {
    switch(tt.nextToken()) {
        case '?':
            {
                StringBuilder buf = new StringBuilder();
                buf.append('?');
                int ch = tt.nextChar();
                for (; ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9'; ch = tt.nextChar()) {
                    buf.append((char) ch);
                }
                tt.pushCharBack(ch);
                return buf.toString();
            }
        case '$':
            {
                StringBuilder buf = new StringBuilder();
                buf.append('$');
                int ch = tt.nextChar();
                for (; ch >= '0' && ch <= '9'; ch = tt.nextChar()) {
                    buf.append((char) ch);
                }
                tt.pushCharBack(ch);
                return buf.toString();
            }
        case StreamPosTokenizer.TT_NUMBER:
            return tt.nval;
        default:
            throw new IOException("coordinate missing at position" + tt.getStartPosition() + " in " + str);
    }
}
