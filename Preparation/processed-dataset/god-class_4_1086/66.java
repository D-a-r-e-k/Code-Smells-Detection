/** Get the content from a stream applying the required filters.
     * @param stream the stream
     * @throws IOException on error
     * @return the stream content
     */
public static byte[] getStreamBytes(PRStream stream) throws IOException {
    RandomAccessFileOrArray rf = stream.getReader().getSafeFile();
    try {
        rf.reOpen();
        return getStreamBytes(stream, rf);
    } finally {
        try {
            rf.close();
        } catch (Exception e) {
        }
    }
}
