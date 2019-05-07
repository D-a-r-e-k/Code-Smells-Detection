/**
     * Reads an SVG "line" element.
     */
private Figure readLineElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    readTransformAttribute(elem, a);
    readOpacityAttribute(elem, a);
    readLineAttributes(elem, a);
    // Because 'line' elements are single lines and thus are geometrically 
    // one-dimensional, they have no interior; thus, 'line' elements are 
    // never filled (see the 'fill' property). 
    if (FILL_COLOR.get(a) != null && STROKE_COLOR.get(a) == null) {
        STROKE_COLOR.put(a, FILL_COLOR.get(a));
    }
    if (FILL_GRADIENT.get(a) != null && STROKE_GRADIENT.get(a) == null) {
        STROKE_GRADIENT.put(a, FILL_GRADIENT.get(a));
    }
    FILL_COLOR.put(a, null);
    FILL_GRADIENT.put(a, null);
    double x1 = toNumber(elem, readAttribute(elem, "x1", "0"));
    double y1 = toNumber(elem, readAttribute(elem, "y1", "0"));
    double x2 = toNumber(elem, readAttribute(elem, "x2", "0"));
    double y2 = toNumber(elem, readAttribute(elem, "y2", "0"));
    Figure figure = factory.createLine(x1, y1, x2, y2, a);
    elementObjects.put(elem, figure);
    return figure;
}
