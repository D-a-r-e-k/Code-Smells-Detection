/** Sets the contents of the page.
     * @param content the new page content
     * @param pageNum the page number. 1 is the first
     * @since	2.1.3	(the method already existed without param compressionLevel)
     */
public void setPageContent(int pageNum, byte content[], int compressionLevel) {
    PdfDictionary page = getPageN(pageNum);
    if (page == null)
        return;
    PdfObject contents = page.get(PdfName.CONTENTS);
    freeXref = -1;
    killXref(contents);
    if (freeXref == -1) {
        xrefObj.add(null);
        freeXref = xrefObj.size() - 1;
    }
    page.put(PdfName.CONTENTS, new PRIndirectReference(this, freeXref));
    xrefObj.set(freeXref, new PRStream(this, content, compressionLevel));
}
