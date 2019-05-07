/**
     * @param obj
     */
public static void releaseLastXrefPartial(PdfObject obj) {
    if (obj == null)
        return;
    if (!obj.isIndirect())
        return;
    if (!(obj instanceof PRIndirectReference))
        return;
    PRIndirectReference ref = (PRIndirectReference) obj;
    PdfReader reader = ref.getReader();
    if (reader.partial && reader.lastXrefPartial != -1 && reader.lastXrefPartial == ref.getNumber()) {
        reader.xrefObj.set(reader.lastXrefPartial, null);
    }
    reader.lastXrefPartial = -1;
}
