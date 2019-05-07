protected void markUsed(PdfObject obj) {
    if (append && obj != null) {
        PRIndirectReference ref = null;
        if (obj.type() == PdfObject.INDIRECT)
            ref = (PRIndirectReference) obj;
        else
            ref = obj.getIndRef();
        if (ref != null)
            marked.put(ref.getNumber(), 1);
    }
}
