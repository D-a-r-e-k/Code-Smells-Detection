/**
     * @param obj
     * @param parent
     * @return a PdfObject
     */
public static PdfObject getPdfObject(PdfObject obj, PdfObject parent) {
    if (obj == null)
        return null;
    if (!obj.isIndirect()) {
        PRIndirectReference ref = null;
        if (parent != null && (ref = parent.getIndRef()) != null && ref.getReader().isAppendable()) {
            switch(obj.type()) {
                case PdfObject.NULL:
                    obj = new PdfNull();
                    break;
                case PdfObject.BOOLEAN:
                    obj = new PdfBoolean(((PdfBoolean) obj).booleanValue());
                    break;
                case PdfObject.NAME:
                    obj = new PdfName(obj.getBytes());
                    break;
            }
            obj.setIndRef(ref);
        }
        return obj;
    }
    return getPdfObject(obj);
}
