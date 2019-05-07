/**
	 * Returns the label bounds of the specified view in the given graph. Note:
	 * The index is the position of the String object for the label in the extra
	 * labels array of the view.
	 */
public Rectangle2D getExtraLabelBounds(JGraph paintingContext, EdgeView view, int index) {
    if (paintingContext == null && graph != null) {
        JGraph graph = (JGraph) this.graph.get();
        paintingContext = graph;
    }
    setView(view);
    Object[] labels = GraphConstants.getExtraLabels(view.getAllAttributes());
    if (labels != null && index < labels.length) {
        Point2D p = getExtraLabelPosition(this.view, index);
        Dimension d = getExtraLabelSize(paintingContext, this.view, index);
        String label = (paintingContext != null) ? paintingContext.convertValueToString(labels[index]) : String.valueOf(labels[index]);
        return getLabelBounds(p, d, label);
    }
    return new Rectangle2D.Double(getX(), getY(), 0, 0);
}
