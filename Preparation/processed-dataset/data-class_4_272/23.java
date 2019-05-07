/**
     * Reads an SVG "textArea" element.
     */
private Figure readTextAreaElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    readTransformAttribute(elem, a);
    readOpacityAttribute(elem, a);
    readShapeAttributes(elem, a);
    readFontAttributes(elem, a);
    readTextAttributes(elem, a);
    readTextFlowAttributes(elem, a);
    double x = toNumber(elem, readAttribute(elem, "x", "0"));
    double y = toNumber(elem, readAttribute(elem, "y", "0"));
    // XXX - Handle "auto" width and height 
    double w = toWidth(elem, readAttribute(elem, "width", "0"));
    double h = toHeight(elem, readAttribute(elem, "height", "0"));
    DefaultStyledDocument doc = new DefaultStyledDocument();
    try {
        if (elem.getContent() != null) {
            doc.insertString(0, toText(elem, elem.getContent()), null);
        } else {
            for (IXMLElement node : elem.getChildren()) {
                if (node.getName() == null) {
                    doc.insertString(doc.getLength(), toText(elem, node.getContent()), null);
                } else if (node.getName().equals("tbreak")) {
                    doc.insertString(doc.getLength(), "\n", null);
                } else if (node.getName().equals("tspan")) {
                    readTSpanElement((IXMLElement) node, doc);
                } else {
                    if (DEBUG) {
                        System.out.println("SVGInputFormat unknown  text node " + node.getName());
                    }
                }
            }
        }
    } catch (BadLocationException e) {
        InternalError ex = new InternalError(e.getMessage());
        ex.initCause(e);
        throw ex;
    }
    Figure figure = factory.createTextArea(x, y, w, h, doc, a);
    elementObjects.put(elem, figure);
    return figure;
}
