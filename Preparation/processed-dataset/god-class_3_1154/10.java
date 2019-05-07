private static String readUntil(Reader reader, char c) throws IOException {
    StringBuilder sb = new StringBuilder();
    int ch;
    while ((ch = read(reader)) != -1) {
        sb.append((char) ch);
        if (ch == c)
            break;
    }
    return sb.toString();
}
