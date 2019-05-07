/**
     * Creates a Line figure.
     */
private ODGFigure createLineFigure(Point2D.Double p1, Point2D.Double p2, Map<AttributeKey, Object> a) throws IOException {
    ODGPathFigure figure = new ODGPathFigure();
    figure.setBounds(p1, p2);
    figure.setAttributes(a);
    return figure;
}
