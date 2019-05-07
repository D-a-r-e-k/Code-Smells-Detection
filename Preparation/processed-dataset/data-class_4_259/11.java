/**
     * Gets the page from a page dictionary
     * @param page the page dictionary
     * @return the page
     */
public Rectangle getPageSize(PdfDictionary page) {
    PdfArray mediaBox = page.getAsArray(PdfName.MEDIABOX);
    return getNormalizedRectangle(mediaBox);
}
