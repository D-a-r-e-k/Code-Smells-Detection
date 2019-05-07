/**
     * use the supplied vv characteristics to set the position and
     * dimensions of the scroll bars. Called in response to
     * a ChangeEvent from the VisualizationViewer
     * @param xform the transform of the VisualizationViewer
     */
private void setScrollBars(VisualizationViewer vv) {
    Dimension d = vv.getGraphLayout().getSize();
    Rectangle2D vvBounds = vv.getBounds();
    // a rectangle representing the layout 
    Rectangle layoutRectangle = new Rectangle(0, 0, d.width, d.height);
    //-d.width/2, -d.height/2, 2*d.width, 2*d.height); 
    BidirectionalTransformer viewTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
    BidirectionalTransformer layoutTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
    Point2D h0 = new Point2D.Double(vvBounds.getMinX(), vvBounds.getCenterY());
    Point2D h1 = new Point2D.Double(vvBounds.getMaxX(), vvBounds.getCenterY());
    Point2D v0 = new Point2D.Double(vvBounds.getCenterX(), vvBounds.getMinY());
    Point2D v1 = new Point2D.Double(vvBounds.getCenterX(), vvBounds.getMaxY());
    h0 = viewTransformer.inverseTransform(h0);
    h0 = layoutTransformer.inverseTransform(h0);
    h1 = viewTransformer.inverseTransform(h1);
    h1 = layoutTransformer.inverseTransform(h1);
    v0 = viewTransformer.inverseTransform(v0);
    v0 = layoutTransformer.inverseTransform(v0);
    v1 = viewTransformer.inverseTransform(v1);
    v1 = layoutTransformer.inverseTransform(v1);
    scrollBarsMayControlAdjusting = false;
    setScrollBarValues(layoutRectangle, h0, h1, v0, v1);
    scrollBarsMayControlAdjusting = true;
}
