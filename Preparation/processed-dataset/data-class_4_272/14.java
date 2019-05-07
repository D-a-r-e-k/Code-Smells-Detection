/**
     * Reads an SVG "rect" element.
     */
private Figure readRectElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    readTransformAttribute(elem, a);
    readOpacityAttribute(elem, a);
    readShapeAttributes(elem, a);
    double x = toNumber(elem, readAttribute(elem, "x", "0"));
    double y = toNumber(elem, readAttribute(elem, "y", "0"));
    double w = toWidth(elem, readAttribute(elem, "width", "0"));
    double h = toHeight(elem, readAttribute(elem, "height", "0"));
    String rxValue = readAttribute(elem, "rx", "none");
    String ryValue = readAttribute(elem, "ry", "none");
    if (rxValue.equals("none")) {
        rxValue = ryValue;
    }
    if (ryValue.equals("none")) {
        ryValue = rxValue;
    }
    double rx = toNumber(elem, rxValue.equals("none") ? "0" : rxValue);
    double ry = toNumber(elem, ryValue.equals("none") ? "0" : ryValue);
    Figure figure = factory.createRect(x, y, w, h, rx, ry, a);
    elementObjects.put(elem, figure);
    return figure;
}
