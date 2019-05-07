/**
     * Reads an SVG "circle" element.
     */
private Figure readCircleElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    readTransformAttribute(elem, a);
    readOpacityAttribute(elem, a);
    readShapeAttributes(elem, a);
    double cx = toWidth(elem, readAttribute(elem, "cx", "0"));
    double cy = toHeight(elem, readAttribute(elem, "cy", "0"));
    double r = toWidth(elem, readAttribute(elem, "r", "0"));
    Figure figure = factory.createCircle(cx, cy, r, a);
    elementObjects.put(elem, figure);
    return figure;
}
