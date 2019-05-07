/** Gets the contents of the page.
     * @param pageNum the page number. 1 is the first
     * @param file the location of the PDF document
     * @throws IOException on error
     * @return the content
     */
public byte[] getPageContent(int pageNum, RandomAccessFileOrArray file) throws IOException {
    PdfDictionary page = getPageNRelease(pageNum);
    if (page == null)
        return null;
    PdfObject contents = getPdfObjectRelease(page.get(PdfName.CONTENTS));
    if (contents == null)
        return new byte[0];
    ByteArrayOutputStream bout = null;
    if (contents.isStream()) {
        return getStreamBytes((PRStream) contents, file);
    } else if (contents.isArray()) {
        PdfArray array = (PdfArray) contents;
        bout = new ByteArrayOutputStream();
        for (int k = 0; k < array.size(); ++k) {
            PdfObject item = getPdfObjectRelease(array.getPdfObject(k));
            if (item == null || !item.isStream())
                continue;
            byte[] b = getStreamBytes((PRStream) item, file);
            bout.write(b);
            if (k != array.size() - 1)
                bout.write('\n');
        }
        return bout.toByteArray();
    } else
        return new byte[0];
}
