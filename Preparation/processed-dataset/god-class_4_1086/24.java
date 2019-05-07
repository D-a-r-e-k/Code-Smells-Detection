/**
     * @param idx
     * @return a PdfObject
     */
public PdfObject getPdfObjectRelease(int idx) {
    PdfObject obj = getPdfObject(idx);
    releaseLastXrefPartial();
    return obj;
}
