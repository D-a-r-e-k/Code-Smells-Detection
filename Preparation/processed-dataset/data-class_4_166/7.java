/**
	 * Calculates the angle at which graphics should be rotated to paint label
	 * along the edge. Before calling this method always check that transform
	 * should be applied using {@linkisLabelTransform}
	 * 
	 * @return the value of the angle, 0 if the angle is zero or can't be
	 *         calculated
	 */
private double getLabelAngle(String label) {
    Point2D p = getLabelPosition(view);
    double angle = 0;
    if (p != null && label != null && label.length() > 0) {
        int sw = metrics.stringWidth(label);
        // Note: For control points you may want to choose other 
        // points depending on the segment the label is in. 
        Point2D p1 = view.getPoint(0);
        Point2D p2 = view.getPoint(view.getPointCount() - 1);
        // Length of the edge 
        double length = Math.sqrt((p2.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (p2.getY() - p1.getY()) * (p2.getY() - p1.getY()));
        if (!(length <= Double.NaN || length < sw)) {
            // Label fits into 
            // edge's length 
            // To calculate projections of edge 
            double cos = (p2.getX() - p1.getX()) / length;
            double sin = (p2.getY() - p1.getY()) / length;
            // Determine angle 
            angle = Math.acos(cos);
            if (sin < 0) {
                // Second half 
                angle = 2 * Math.PI - angle;
            }
        }
        if (angle > Math.PI / 2 && angle <= Math.PI * 3 / 2) {
            angle -= Math.PI;
        }
    }
    return angle;
}
