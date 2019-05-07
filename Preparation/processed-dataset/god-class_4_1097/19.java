/**
     * Creates a Ellipse figure.
     */
private ODGFigure createEnhancedGeometryEllipseFigure(Rectangle2D.Double bounds, Map<AttributeKey, Object> a) throws IOException {
    ODGEllipseFigure figure = new ODGEllipseFigure();
    figure.setBounds(bounds);
    figure.setAttributes(a);
    return figure;
}
