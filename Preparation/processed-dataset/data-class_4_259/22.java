/**
     * Reads a <CODE>PdfObject</CODE> resolving an indirect reference
     * if needed. If the reader was opened in partial mode the object will be released
     * to save memory.
     * @param obj the <CODE>PdfObject</CODE> to read
     * @param parent
     * @return a PdfObject
     */
public static PdfObject getPdfObjectRelease(PdfObject obj, PdfObject parent) {
    PdfObject obj2 = getPdfObject(obj, parent);
    releaseLastXrefPartial(obj);
    return obj2;
}
