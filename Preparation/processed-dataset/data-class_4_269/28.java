/**
     * The &lt;draw:line&gt; element represents a line.
     * <p>
     * The attributes that may be associated with the &lt;draw:line&gt; element
     * are:
     * • Style, Layer, Z-Index, ID, Caption ID and Transformation – see section
     * 9.2.15.
     * • Text anchor, table background, draw end position– see section 9.2.16.
     * • Start point
     * • End point
     * <p>
     * The elements that may be contained in the &lt;draw:line&gt; element are:
     * • Title (short accessible name) – see section 9.2.20.
     * • Long description (in support of accessibility) – see section 9.2.20.
     * • Event listeners – see section 9.2.21.
     * • Glue points – see section 9.2.19.
     * • Text – see section 9.2.17.
     */
private ODGFigure readLineElement(IXMLElement elem) throws IOException {
    Point2D.Double p1 = new Point2D.Double(toLength(elem.getAttribute("x1", SVG_NAMESPACE, "0"), 1), toLength(elem.getAttribute("y1", SVG_NAMESPACE, "0"), 1));
    Point2D.Double p2 = new Point2D.Double(toLength(elem.getAttribute("x2", SVG_NAMESPACE, "0"), 1), toLength(elem.getAttribute("y2", SVG_NAMESPACE, "0"), 1));
    String styleName = elem.getAttribute("style-name", DRAWING_NAMESPACE, null);
    Map<AttributeKey, Object> a = styles.getAttributes(styleName, "graphic");
    ODGFigure f = createLineFigure(p1, p2, a);
    return f;
}
