/**
     * The &lt;draw:polygon&gt; element represents a polygon. A polygon is a
     * closed put of straight lines.
     * Some implementations may ignore the size attribute, and instead determine
     * the size of a shape exclusively from the shape data (i.e., polygon vertices).
     * <p>
     * The attributes that may be associated with the &lt;draw:polygon&gt; element
     * are:
     * • Position, Size, View box, Style, Layer, Z-Index, ID, Caption ID and
     * Transformation – see section 9.2.15
     * • Text anchor, table background, draw end position – see section 9.2.16
     * • Points – see section 9.2.3
     * The elements that may be contained in the &lt;draw:polygon&gt; element are:
     * • Title (short accessible name) – see section 9.2.20.
     * • Long description (in support of accessibility) – see section 9.2.20.
     * • Event listeners – see section 9.2.21.
     * • Glue points – see section 9.2.19.
     * • Text – see section 9.2.17.
     *
     */
private ODGFigure readPolygonElement(IXMLElement elem) throws IOException {
    AffineTransform viewBoxTransform = readViewBoxTransform(elem);
    String[] coords = toWSOrCommaSeparatedArray(elem.getAttribute("points", DRAWING_NAMESPACE, null));
    Point2D.Double[] points = new Point2D.Double[coords.length / 2];
    for (int i = 0; i < coords.length; i += 2) {
        Point2D.Double p = new Point2D.Double(toNumber(coords[i]), toNumber(coords[i + 1]));
        points[i / 2] = (Point2D.Double) viewBoxTransform.transform(p, p);
    }
    String styleName = elem.getAttribute("style-name", DRAWING_NAMESPACE, null);
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    a.putAll(styles.getAttributes(styleName, "graphic"));
    readCommonDrawingShapeAttributes(elem, a);
    ODGFigure f = createPolygonFigure(points, a);
    return f;
}
