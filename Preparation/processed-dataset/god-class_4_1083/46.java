/**
     * Sets the open and close page additional action.
     * @param actionType the action type. It can be <CODE>PdfWriter.PAGE_OPEN</CODE>
     * or <CODE>PdfWriter.PAGE_CLOSE</CODE>
     * @param action the action to perform
     * @param page the page where the action will be applied. The first page is 1
     * @throws PdfException if the action type is invalid
     */
void setPageAction(PdfName actionType, PdfAction action, int page) throws PdfException {
    if (!actionType.equals(PAGE_OPEN) && !actionType.equals(PAGE_CLOSE))
        throw new PdfException(MessageLocalization.getComposedMessage("invalid.page.additional.action.type.1", actionType.toString()));
    PdfDictionary pg = reader.getPageN(page);
    PdfDictionary aa = (PdfDictionary) PdfReader.getPdfObject(pg.get(PdfName.AA), pg);
    if (aa == null) {
        aa = new PdfDictionary();
        pg.put(PdfName.AA, aa);
        markUsed(pg);
    }
    aa.put(actionType, action);
    markUsed(aa);
}
