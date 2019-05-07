/**
     * Reads an SVG "a" element.
     */
private Figure readAElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    CompositeFigure g = factory.createG(a);
    String href = readAttribute(elem, "xlink:href", null);
    if (href == null) {
        href = readAttribute(elem, "href", null);
    }
    String target = readAttribute(elem, "target", null);
    if (DEBUG) {
        System.out.println("SVGInputFormat.readAElement href=" + href);
    }
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
            if (childFigure != null) {
                childFigure.set(LINK, href);
                childFigure.set(LINK_TARGET, target);
            } else {
                if (DEBUG) {
                    System.out.println("SVGInputFormat <a> has no child figure");
                }
            }
        }
    }
    return (g.getChildCount() == 1) ? g.getChild(0) : g;
}
