/** Sets the contents of the page.
     * @param content the new page content
     * @param pageNum the page number. 1 is the first
     */
public void setPageContent(int pageNum, byte content[]) {
    setPageContent(pageNum, content, PdfStream.DEFAULT_COMPRESSION);
}
