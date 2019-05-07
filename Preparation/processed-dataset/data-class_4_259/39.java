/**
     * Eliminates the reference to the object freeing the memory used by it and clearing
     * the xref entry.
     * @param obj the object. If it's an indirect reference it will be eliminated
     * @return the object or the already erased dereferenced object
     */
public static PdfObject killIndirect(PdfObject obj) {
    if (obj == null || obj.isNull())
        return null;
    PdfObject ret = getPdfObjectRelease(obj);
    if (obj.isIndirect()) {
        PRIndirectReference ref = (PRIndirectReference) obj;
        PdfReader reader = ref.getReader();
        int n = ref.getNumber();
        reader.xrefObj.set(n, null);
        if (reader.partial)
            reader.xref[n * 2] = -1;
    }
    return ret;
}
