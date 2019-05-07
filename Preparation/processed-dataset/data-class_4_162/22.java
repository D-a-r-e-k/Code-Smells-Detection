/**
     *  Reads the stream while the characters that have been specified are
     *  in the stream, returning then the result as a String.
     */
private String readWhile(String endChars) throws IOException {
    StringBuilder sb = new StringBuilder(80);
    int ch = nextToken();
    while (ch != -1) {
        if (endChars.indexOf((char) ch) == -1) {
            pushBack(ch);
            break;
        }
        sb.append((char) ch);
        ch = nextToken();
    }
    return sb.toString();
}
