/**
     * Creates a Polygon figure.
     */
private ODGFigure createPolygonFigure(Point2D.Double[] points, Map<AttributeKey, Object> a) throws IOException {
    ODGPathFigure figure = new ODGPathFigure();
    ODGBezierFigure bezier = new ODGBezierFigure();
    for (Point2D.Double p : points) {
        bezier.addNode(new BezierPath.Node(p.x, p.y));
    }
    bezier.setClosed(true);
    figure.removeAllChildren();
    figure.add(bezier);
    figure.setAttributes(a);
    return figure;
}
