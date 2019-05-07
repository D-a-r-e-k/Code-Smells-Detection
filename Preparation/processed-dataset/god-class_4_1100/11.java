/**
     * Reads an SVG "g" element.
     */
private Figure readGElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    readOpacityAttribute(elem, a);
    CompositeFigure g = factory.createG(a);
    for (IXMLElement node : elem.getChildren()) {
        if (node instanceof IXMLElement) {
            IXMLElement child = (IXMLElement) node;
            Figure childFigure = readElement(child);
            // skip invisible elements 
            if (readAttribute(child, "visibility", "visible").equals("visible") && !readAttribute(child, "display", "inline").equals("none")) {
                if (childFigure != null) {
                    g.basicAdd(childFigure);
                }
            }
        }
    }
    readTransformAttribute(elem, a);
    if (TRANSFORM.get(a) != null) {
        g.transform(TRANSFORM.get(a));
    }
    return g;
}
