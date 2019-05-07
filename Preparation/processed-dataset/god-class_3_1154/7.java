private static int read(Reader reader) throws IOException {
    int ch = -1;
    while ((ch = reader.read()) != -1) {
        if (!Character.isWhitespace(ch))
            return ch;
    }
    return ch;
}
