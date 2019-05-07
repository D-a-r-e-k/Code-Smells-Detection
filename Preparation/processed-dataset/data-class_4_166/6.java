/**
	 * Estimates whether the transform for label should be applied. With the
	 * transform, the label will be painted along the edge. To apply transform,
	 * rotate graphics by the angle returned from {@link #getLabelAngle}
	 * 
	 * @return true, if transform can be applied, false otherwise
	 */
private boolean isLabelTransform(String label) {
    if (!isLabelTransformEnabled()) {
        return false;
    }
    Point2D p = getLabelPosition(view);
    if (p != null && label != null && label.length() > 0) {
        int sw = metrics.stringWidth(label);
        Point2D p1 = view.getPoint(0);
        Point2D p2 = view.getPoint(view.getPointCount() - 1);
        double length = Math.sqrt((p2.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (p2.getY() - p1.getY()) * (p2.getY() - p1.getY()));
        if (!(length <= Double.NaN || length < sw)) {
            return true;
        }
    }
    return false;
}
