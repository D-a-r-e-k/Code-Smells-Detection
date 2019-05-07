/**
     * Creates a Path figure.
     */
private ODGFigure createPathFigure(BezierPath[] paths, Map<AttributeKey, Object> a) throws IOException {
    ODGPathFigure figure = new ODGPathFigure();
    figure.removeAllChildren();
    for (BezierPath p : paths) {
        ODGBezierFigure bezier = new ODGBezierFigure();
        bezier.setBezierPath(p);
        figure.add(bezier);
    }
    figure.setAttributes(a);
    return figure;
}
