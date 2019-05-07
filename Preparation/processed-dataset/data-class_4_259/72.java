/** Gets the XML metadata.
     * @throws IOException on error
     * @return the XML metadata
     */
public byte[] getMetadata() throws IOException {
    PdfObject obj = getPdfObject(catalog.get(PdfName.METADATA));
    if (!(obj instanceof PRStream))
        return null;
    RandomAccessFileOrArray rf = getSafeFile();
    byte b[] = null;
    try {
        rf.reOpen();
        b = getStreamBytes((PRStream) obj, rf);
    } finally {
        try {
            rf.close();
        } catch (Exception e) {
        }
    }
    return b;
}
