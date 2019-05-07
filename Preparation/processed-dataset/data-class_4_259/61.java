/** Gets the contents of the page.
     * @param pageNum the page number. 1 is the first
     * @throws IOException on error
     * @return the content
     */
public byte[] getPageContent(int pageNum) throws IOException {
    RandomAccessFileOrArray rf = getSafeFile();
    try {
        rf.reOpen();
        return getPageContent(pageNum, rf);
    } finally {
        try {
            rf.close();
        } catch (Exception e) {
        }
    }
}
