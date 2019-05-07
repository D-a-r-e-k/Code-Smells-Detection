/**
     * Creates a Rect figure.
     */
private ODGFigure createEnhancedGeometryRectangleFigure(Rectangle2D.Double bounds, Map<AttributeKey, Object> a) throws IOException {
    ODGRectFigure figure = new ODGRectFigure();
    figure.setBounds(bounds);
    figure.setAttributes(a);
    return figure;
}
