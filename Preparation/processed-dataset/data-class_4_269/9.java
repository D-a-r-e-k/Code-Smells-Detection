/**
     * Reads all bytes from the InputStreams until EOF is reached.
     */
private byte[] readAllBytes(InputStream in) throws IOException {
    ByteArrayOutputStream tmp = new ByteArrayOutputStream();
    byte[] buf = new byte[512];
    for (int len; -1 != (len = in.read(buf)); ) {
        tmp.write(buf, 0, len);
    }
    tmp.close();
    return tmp.toByteArray();
}
