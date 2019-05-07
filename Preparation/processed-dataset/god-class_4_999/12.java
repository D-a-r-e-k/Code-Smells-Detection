/**
	 * Returns the label position of the specified view in the given graph.
	 */
public Point2D getExtraLabelPosition(EdgeView view, int index) {
    setView(view);
    Point2D[] pts = GraphConstants.getExtraLabelPositions(view.getAllAttributes());
    if (pts != null && index < pts.length)
        return getLabelPosition(pts[index]);
    return null;
}
