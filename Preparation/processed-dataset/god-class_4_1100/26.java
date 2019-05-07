/**
     * Reads an SVG "use" element.
     */
@SuppressWarnings("unchecked")
private Figure readUseElement(IXMLElement elem) throws IOException {
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    readCoreAttributes(elem, a);
    HashMap<AttributeKey, Object> a2 = new HashMap<AttributeKey, Object>();
    readTransformAttribute(elem, a);
    readOpacityAttribute(elem, a2);
    readUseShapeAttributes(elem, a2);
    readFontAttributes(elem, a2);
    String href = readAttribute(elem, "xlink:href", null);
    if (href != null && href.startsWith("#")) {
        IXMLElement refElem = identifiedElements.get(href.substring(1));
        if (refElem == null) {
            if (DEBUG) {
                System.out.println("SVGInputFormat couldn't find href for <use> element:" + href);
            }
        } else {
            Object obj = readElement(refElem);
            if (obj instanceof Figure) {
                Figure figure = (Figure) ((Figure) obj).clone();
                for (Map.Entry<AttributeKey, Object> entry : a2.entrySet()) {
                    figure.set(entry.getKey(), entry.getValue());
                }
                AffineTransform tx = (TRANSFORM.get(a) == null) ? new AffineTransform() : TRANSFORM.get(a);
                double x = toNumber(elem, readAttribute(elem, "x", "0"));
                double y = toNumber(elem, readAttribute(elem, "y", "0"));
                tx.translate(x, y);
                figure.transform(tx);
                return figure;
            }
        }
    }
    return null;
}
