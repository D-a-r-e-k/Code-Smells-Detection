/**
	 * Returns the label position of the specified view in the given graph.
	 */
public Point2D getLabelPosition(EdgeView view) {
    setView(view);
    return getLabelPosition(view.getLabelPosition());
}
