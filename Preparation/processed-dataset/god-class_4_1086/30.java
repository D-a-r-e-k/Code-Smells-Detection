/**
     * @param obj
     * @return an indirect reference
     */
public PRIndirectReference addPdfObject(PdfObject obj) {
    xrefObj.add(obj);
    return new PRIndirectReference(this, xrefObj.size() - 1);
}
