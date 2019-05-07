/**
     *  Reads the stream until the current brace is closed or stream end.
     */
private String readBraceContent(char opening, char closing) throws IOException {
    StringBuilder sb = new StringBuilder(40);
    int braceLevel = 1;
    int ch;
    while ((ch = nextToken()) != -1) {
        if (ch == '\\') {
            continue;
        } else if (ch == opening) {
            braceLevel++;
        } else if (ch == closing) {
            braceLevel--;
            if (braceLevel == 0) {
                break;
            }
        }
        sb.append((char) ch);
    }
    return sb.toString();
}
