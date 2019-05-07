/**
     * @param obj
     * @return a PdfObject
     */
public static PdfObject getPdfObjectRelease(PdfObject obj) {
    PdfObject obj2 = getPdfObject(obj);
    releaseLastXrefPartial(obj);
    return obj2;
}
