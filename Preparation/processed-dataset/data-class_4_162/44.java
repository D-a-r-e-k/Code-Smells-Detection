/**
     *  Reads the stream until the next EOL or EOF.  Note that it will also read the
     *  EOL from the stream.
     */
private StringBuilder readUntilEOL() throws IOException {
    int ch;
    StringBuilder buf = new StringBuilder(256);
    while (true) {
        ch = nextToken();
        if (ch == -1)
            break;
        buf.append((char) ch);
        if (ch == '\n')
            break;
    }
    return buf;
}
