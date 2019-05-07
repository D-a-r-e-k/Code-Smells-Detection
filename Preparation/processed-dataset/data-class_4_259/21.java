/**
     * Reads a <CODE>PdfObject</CODE> resolving an indirect reference
     * if needed.
     * @param obj the <CODE>PdfObject</CODE> to read
     * @return the resolved <CODE>PdfObject</CODE>
     */
public static PdfObject getPdfObject(PdfObject obj) {
    if (obj == null)
        return null;
    if (!obj.isIndirect())
        return obj;
    try {
        PRIndirectReference ref = (PRIndirectReference) obj;
        int idx = ref.getNumber();
        boolean appendable = ref.getReader().appendable;
        obj = ref.getReader().getPdfObject(idx);
        if (obj == null) {
            return null;
        } else {
            if (appendable) {
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
    } catch (Exception e) {
        throw new ExceptionConverter(e);
    }
}
