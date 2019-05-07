/**
     *  Reads the stream until it meets one of the specified
     *  ending characters, or stream end.  The ending character will be left
     *  in the stream.
     */
private String readUntil(String endChars) throws IOException {
    StringBuilder sb = new StringBuilder(80);
    int ch = nextToken();
    while (ch != -1) {
        if (ch == '\\') {
            ch = nextToken();
            if (ch == -1) {
                break;
            }
        } else {
            if (endChars.indexOf((char) ch) != -1) {
                pushBack(ch);
                break;
            }
        }
        sb.append((char) ch);
        ch = nextToken();
    }
    return sb.toString();
}
