private ODGFigure readPathElement(IXMLElement elem) throws IOException {
    AffineTransform viewBoxTransform = readViewBoxTransform(elem);
    BezierPath[] paths = toPath(elem.getAttribute("d", SVG_NAMESPACE, null));
    for (BezierPath p : paths) {
        p.transform(viewBoxTransform);
    }
    String styleName = elem.getAttribute("style-name", DRAWING_NAMESPACE, null);
    HashMap<AttributeKey, Object> a = new HashMap<AttributeKey, Object>();
    a.putAll(styles.getAttributes(styleName, "graphic"));
    readCommonDrawingShapeAttributes(elem, a);
    ODGFigure f = createPathFigure(paths, a);
    return f;
}
