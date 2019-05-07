/**
	 * Returns the label position of the specified view in the given graph.
	 */
protected Point2D getLabelPosition(Point2D pos) {
    Rectangle2D tmp = getPaintBounds(view);
    int unit = GraphConstants.PERMILLE;
    Point2D p0 = view.getPoint(0);
    if (pos != null && tmp != null && p0 != null) {
        if (!isLabelTransformEnabled()) {
            return view.getAbsoluteLabelPositionFromRelative(pos);
        } else {
            Point2D vector = view.getLabelVector();
            double dx = vector.getX();
            double dy = vector.getY();
            double len = Math.sqrt(dx * dx + dy * dy);
            if (len > 0) {
                int pointIndex = view.getFirstPointOfSegment();
                if (pointIndex >= 0 && pointIndex < view.getPointCount() - 1) {
                    p0 = view.getPoint(pointIndex);
                }
                double x = p0.getX() + (dx * pos.getX() / unit);
                double y = p0.getY() + (dy * pos.getX() / unit);
                x += (-dy * pos.getY() / len);
                y += (dx * pos.getY() / len);
                return new Point2D.Double(x, y);
            } else {
                return new Point2D.Double(p0.getX() + pos.getX(), p0.getY() + pos.getY());
            }
        }
    }
    return null;
}
