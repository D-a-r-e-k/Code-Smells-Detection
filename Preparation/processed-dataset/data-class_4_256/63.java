/**
     * Gets the PdfLayer objects in an existing document as a Map
     * with the names/titles of the layers as keys.
     * @return	a Map with all the PdfLayers in the document (and the name/title of the layer as key)
     * @since	2.1.2
     */
public Map<String, PdfLayer> getPdfLayers() {
    if (documentOCG.isEmpty()) {
        readOCProperties();
    }
    HashMap<String, PdfLayer> map = new HashMap<String, PdfLayer>();
    PdfLayer layer;
    String key;
    for (PdfOCG pdfOCG : documentOCG) {
        layer = (PdfLayer) pdfOCG;
        if (layer.getTitle() == null) {
            key = layer.getAsString(PdfName.NAME).toString();
        } else {
            key = layer.getTitle();
        }
        if (map.containsKey(key)) {
            int seq = 2;
            String tmp = key + "(" + seq + ")";
            while (map.containsKey(tmp)) {
                seq++;
                tmp = key + "(" + seq + ")";
            }
            key = tmp;
        }
        map.put(key, layer);
    }
    return map;
}
