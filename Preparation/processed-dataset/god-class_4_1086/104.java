/**
     * Closes the reader
     */
public void close() {
    if (!partial)
        return;
    try {
        tokens.close();
    } catch (IOException e) {
        throw new ExceptionConverter(e);
    }
}
