/**
     * Reads the OCProperties dictionary from the catalog of the existing document
     * and fills the documentOCG, documentOCGorder and OCGRadioGroup variables in PdfWriter.
     * Note that the original OCProperties of the existing document can contain more information.
     * @since	2.1.2
     */
protected void readOCProperties() {
    if (!documentOCG.isEmpty()) {
        return;
    }
    PdfDictionary dict = reader.getCatalog().getAsDict(PdfName.OCPROPERTIES);
    if (dict == null) {
        return;
    }
    PdfArray ocgs = dict.getAsArray(PdfName.OCGS);
    PdfIndirectReference ref;
    PdfLayer layer;
    HashMap<String, PdfLayer> ocgmap = new HashMap<String, PdfLayer>();
    for (Iterator<PdfObject> i = ocgs.listIterator(); i.hasNext(); ) {
        ref = (PdfIndirectReference) i.next();
        layer = new PdfLayer(null);
        layer.setRef(ref);
        layer.setOnPanel(false);
        layer.merge((PdfDictionary) PdfReader.getPdfObject(ref));
        ocgmap.put(ref.toString(), layer);
    }
    PdfDictionary d = dict.getAsDict(PdfName.D);
    PdfArray off = d.getAsArray(PdfName.OFF);
    if (off != null) {
        for (Iterator<PdfObject> i = off.listIterator(); i.hasNext(); ) {
            ref = (PdfIndirectReference) i.next();
            layer = ocgmap.get(ref.toString());
            layer.setOn(false);
        }
    }
    PdfArray order = d.getAsArray(PdfName.ORDER);
    if (order != null) {
        addOrder(null, order, ocgmap);
    }
    documentOCG.addAll(ocgmap.values());
    OCGRadioGroup = d.getAsArray(PdfName.RBGROUPS);
    OCGLocked = d.getAsArray(PdfName.LOCKED);
    if (OCGLocked == null)
        OCGLocked = new PdfArray();
}
