/** Get the content from a stream as it is without applying any filter.
     * @param stream the stream
     * @throws IOException on error
     * @return the stream content
     */
public static byte[] getStreamBytesRaw(PRStream stream) throws IOException {
    RandomAccessFileOrArray rf = stream.getReader().getSafeFile();
    try {
        rf.reOpen();
        return getStreamBytesRaw(stream, rf);
    } finally {
        try {
            rf.close();
        } catch (Exception e) {
        }
    }
}
