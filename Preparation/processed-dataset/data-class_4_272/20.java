/**
     * Reads an SVG "polygon" element.
     */
private Figure readPolygonElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    readTransformAttribute(elem, a);
    readOpacityAttribute(elem, a);
    readShapeAttributes(elem, a);
    Point2D.Double[] points = toPoints(elem, readAttribute(elem, "points", ""));
    Figure figure = factory.createPolygon(points, a);
    elementObjects.put(elem, figure);
    return figure;
}
