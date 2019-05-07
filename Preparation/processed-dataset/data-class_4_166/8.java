/**
	 * Returns the label bounds of the specified view in the given graph.
	 */
public Rectangle2D getLabelBounds(JGraph paintingContext, EdgeView view) {
    if (paintingContext == null && graph != null) {
        JGraph graph = (JGraph) this.graph.get();
        paintingContext = graph;
    }
    // No need to call setView as getLabelPosition will 
    String label = (paintingContext != null) ? paintingContext.convertValueToString(view) : String.valueOf(view.getCell());
    if (label != null) {
        Point2D p = getLabelPosition(view);
        Dimension d = getLabelSize(view, label);
        return getLabelBounds(p, d, label);
    } else {
        return null;
    }
}
