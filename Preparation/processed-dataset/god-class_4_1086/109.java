/**
     * Gets the global document JavaScript.
     * @throws IOException on error
     * @return the global document JavaScript
     */
public String getJavaScript() throws IOException {
    RandomAccessFileOrArray rf = getSafeFile();
    try {
        rf.reOpen();
        return getJavaScript(rf);
    } finally {
        try {
            rf.close();
        } catch (Exception e) {
        }
    }
}
