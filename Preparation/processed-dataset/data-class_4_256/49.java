/**
     * Sets the display duration for the page (for presentations)
     * @param seconds   the number of seconds to display the page. A negative value removes the entry
     * @param page the page where the duration will be applied. The first page is 1
     */
void setDuration(int seconds, int page) {
    PdfDictionary pg = reader.getPageN(page);
    if (seconds < 0)
        pg.remove(PdfName.DUR);
    else
        pg.put(PdfName.DUR, new PdfNumber(seconds));
    markUsed(pg);
}
