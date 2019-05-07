/**
     * Reads an SVG "ellipse" element.
     */
private Figure readEllipseElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    readTransformAttribute(elem, a);
    readOpacityAttribute(elem, a);
    readShapeAttributes(elem, a);
    double cx = toWidth(elem, readAttribute(elem, "cx", "0"));
    double cy = toHeight(elem, readAttribute(elem, "cy", "0"));
    double rx = toWidth(elem, readAttribute(elem, "rx", "0"));
    double ry = toHeight(elem, readAttribute(elem, "ry", "0"));
    Figure figure = factory.createEllipse(cx, cy, rx, ry, a);
    elementObjects.put(elem, figure);
    return figure;
}
