/**
     * Reads an SVG "polyline" element.
     */
private Figure readPolylineElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    readTransformAttribute(elem, a);
    readOpacityAttribute(elem, a);
    readLineAttributes(elem, a);
    Point2D.Double[] points = toPoints(elem, readAttribute(elem, "points", ""));
    Figure figure = factory.createPolyline(points, a);
    elementObjects.put(elem, figure);
    return figure;
}
