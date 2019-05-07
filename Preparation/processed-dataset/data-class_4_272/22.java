/**
     * Reads an SVG "text" element.
     */
private Figure readTextElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    readTransformAttribute(elem, a);
    readOpacityAttribute(elem, a);
    readShapeAttributes(elem, a);
    readFontAttributes(elem, a);
    readTextAttributes(elem, a);
    String[] xStr = toCommaSeparatedArray(readAttribute(elem, "x", "0"));
    String[] yStr = toCommaSeparatedArray(readAttribute(elem, "y", "0"));
    Point2D.Double[] coordinates = new Point2D.Double[Math.max(xStr.length, yStr.length)];
    double lastX = 0;
    double lastY = 0;
    for (int i = 0; i < coordinates.length; i++) {
        if (xStr.length > i) {
            try {
                lastX = toNumber(elem, xStr[i]);
            } catch (NumberFormatException ex) {
            }
        }
        if (yStr.length > i) {
            try {
                lastY = toNumber(elem, yStr[i]);
            } catch (NumberFormatException ex) {
            }
        }
        coordinates[i] = new Point2D.Double(lastX, lastY);
    }
    String[] rotateStr = toCommaSeparatedArray(readAttribute(elem, "rotate", ""));
    double[] rotate = new double[rotateStr.length];
    for (int i = 0; i < rotateStr.length; i++) {
        try {
            rotate[i] = toDouble(elem, rotateStr[i]);
        } catch (NumberFormatException ex) {
            rotate[i] = 0;
        }
    }
    DefaultStyledDocument doc = new DefaultStyledDocument();
    try {
        if (elem.getContent() != null) {
            doc.insertString(0, toText(elem, elem.getContent()), null);
        } else {
            for (IXMLElement node : elem.getChildren()) {
                if (node.getName() == null) {
                    doc.insertString(0, toText(elem, node.getContent()), null);
                } else if (node.getName().equals("tspan")) {
                    readTSpanElement((IXMLElement) node, doc);
                } else {
                    if (DEBUG) {
                        System.out.println("SVGInputFormat unsupported text node <" + node.getName() + ">");
                    }
                }
            }
        }
    } catch (BadLocationException e) {
        InternalError ex = new InternalError(e.getMessage());
        ex.initCause(e);
        throw ex;
    }
    Figure figure = factory.createText(coordinates, rotate, doc, a);
    elementObjects.put(elem, figure);
    return figure;
}
