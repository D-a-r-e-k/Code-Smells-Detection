/**
	 * Returns the bounds of the edge shape.
	 */
public Rectangle2D getBounds(CellView value) {
    if (value instanceof EdgeView && value != null) {
        // No need to call setView as getPaintBounds will 
        view = (EdgeView) value;
        Rectangle2D r = getPaintBounds(view);
        JGraph graph = null;
        if (this.graph != null) {
            graph = (JGraph) this.graph.get();
        }
        Rectangle2D rect = getLabelBounds(graph, view);
        if (rect != null)
            Rectangle2D.union(r, rect, r);
        Object[] labels = GraphConstants.getExtraLabels(view.getAllAttributes());
        if (labels != null) {
            for (int i = 0; i < labels.length; i++) {
                rect = getExtraLabelBounds(graph, view, i);
                if (rect != null)
                    Rectangle2D.union(r, rect, r);
            }
        }
        int b = (int) Math.ceil(lineWidth);
        r.setFrame(r.getX() - b, r.getY() - b, r.getWidth() + 2 * b, r.getHeight() + 2 * b);
        return r;
    }
    return null;
}
