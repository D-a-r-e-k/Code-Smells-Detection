/**
     * Recursive method to reconstruct the documentOCGorder variable in the writer.
     * @param	parent	a parent PdfLayer (can be null)
     * @param	arr		an array possibly containing children for the parent PdfLayer
     * @param	ocgmap	a HashMap with indirect reference Strings as keys and PdfLayer objects as values.
     * @since	2.1.2
     */
private void addOrder(PdfLayer parent, PdfArray arr, Map<String, PdfLayer> ocgmap) {
    PdfObject obj;
    PdfLayer layer;
    for (int i = 0; i < arr.size(); i++) {
        obj = arr.getPdfObject(i);
        if (obj.isIndirect()) {
            layer = ocgmap.get(obj.toString());
            layer.setOnPanel(true);
            registerLayer(layer);
            if (parent != null) {
                parent.addChild(layer);
            }
            if (arr.size() > i + 1 && arr.getPdfObject(i + 1).isArray()) {
                i++;
                addOrder(layer, (PdfArray) arr.getPdfObject(i), ocgmap);
            }
        } else if (obj.isArray()) {
            PdfArray sub = (PdfArray) obj;
            if (sub.isEmpty())
                return;
            obj = sub.getPdfObject(0);
            if (obj.isString()) {
                layer = new PdfLayer(obj.toString());
                layer.setOnPanel(true);
                registerLayer(layer);
                if (parent != null) {
                    parent.addChild(layer);
                }
                PdfArray array = new PdfArray();
                for (Iterator<PdfObject> j = sub.listIterator(); j.hasNext(); ) {
                    array.add(j.next());
                }
                addOrder(layer, array, ocgmap);
            } else {
                addOrder(parent, (PdfArray) obj, ocgmap);
            }
        }
    }
}
