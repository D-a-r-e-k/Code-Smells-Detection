/**
     * @param idx
     * @return aPdfObject
     */
public PdfObject getPdfObject(int idx) {
    try {
        lastXrefPartial = -1;
        if (idx < 0 || idx >= xrefObj.size())
            return null;
        PdfObject obj = xrefObj.get(idx);
        if (!partial || obj != null)
            return obj;
        if (idx * 2 >= xref.length)
            return null;
        obj = readSingleObject(idx);
        lastXrefPartial = -1;
        if (obj != null)
            lastXrefPartial = idx;
        return obj;
    } catch (Exception e) {
        throw new ExceptionConverter(e);
    }
}
