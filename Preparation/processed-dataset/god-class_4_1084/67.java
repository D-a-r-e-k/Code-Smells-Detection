void setPageAction(PdfName actionType, PdfAction action) {
    if (pageAA == null) {
        pageAA = new PdfDictionary();
    }
    pageAA.put(actionType, action);
}
