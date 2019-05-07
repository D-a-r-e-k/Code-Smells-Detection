/**
     * Reads an SVG "path" element.
     */
private Figure readPathElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    readTransformAttribute(elem, a);
    readOpacityAttribute(elem, a);
    readShapeAttributes(elem, a);
    BezierPath[] beziers = toPath(elem, readAttribute(elem, "d", ""));
    Figure figure = factory.createPath(beziers, a);
    elementObjects.put(elem, figure);
    return figure;
}
