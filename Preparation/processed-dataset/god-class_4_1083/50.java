/**
     * Sets the transition for the page
     * @param transition   the transition object. A <code>null</code> removes the transition
     * @param page the page where the transition will be applied. The first page is 1
     */
void setTransition(PdfTransition transition, int page) {
    PdfDictionary pg = reader.getPageN(page);
    if (transition == null)
        pg.remove(PdfName.TRANS);
    else
        pg.put(PdfName.TRANS, transition.getTransitionDictionary());
    markUsed(pg);
}
