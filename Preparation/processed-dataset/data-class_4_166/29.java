/**
	 * Paint the current view's direction. Sets tmpPoint as a side-effect such
	 * that the invoking method can use it to determine the connection point to
	 * this decoration.
	 */
protected Shape createLineEnd(int size, int style, Point2D src, Point2D dst) {
    if (src == null || dst == null)
        return null;
    int d = (int) Math.max(1, dst.distance(src));
    int ax = (int) -(size * (dst.getX() - src.getX()) / d);
    int ay = (int) -(size * (dst.getY() - src.getY()) / d);
    if (style == GraphConstants.ARROW_DIAMOND) {
        Polygon poly = new Polygon();
        poly.addPoint((int) dst.getX(), (int) dst.getY());
        poly.addPoint((int) (dst.getX() + ax / 2 + ay / 3), (int) (dst.getY() + ay / 2 - ax / 3));
        Point2D last = (Point2D) dst.clone();
        dst.setLocation(dst.getX() + ax, dst.getY() + ay);
        poly.addPoint((int) dst.getX(), (int) dst.getY());
        poly.addPoint((int) (last.getX() + ax / 2 - ay / 3), (int) (last.getY() + ay / 2 + ax / 3));
        return poly;
    } else if (style == GraphConstants.ARROW_TECHNICAL || style == GraphConstants.ARROW_CLASSIC) {
        Polygon poly = new Polygon();
        poly.addPoint((int) dst.getX(), (int) dst.getY());
        poly.addPoint((int) (dst.getX() + ax + ay / 2), (int) (dst.getY() + ay - ax / 2));
        Point2D last = (Point2D) dst.clone();
        if (style == GraphConstants.ARROW_CLASSIC) {
            dst.setLocation((int) (dst.getX() + ax * 2 / 3), (int) (dst.getY() + ay * 2 / 3));
            poly.addPoint((int) dst.getX(), (int) dst.getY());
        } else if (style == GraphConstants.ARROW_DIAMOND) {
            dst.setLocation(dst.getX() + 2 * ax, dst.getY() + 2 * ay);
            poly.addPoint((int) dst.getX(), (int) dst.getY());
        } else
            dst.setLocation((int) (dst.getX() + ax), (int) (dst.getY() + ay));
        poly.addPoint((int) (last.getX() + ax - ay / 2), (int) (last.getY() + ay + ax / 2));
        return poly;
    } else if (style == GraphConstants.ARROW_SIMPLE) {
        GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO, 4);
        path.moveTo((float) (dst.getX() + ax + ay / 2), (float) (dst.getY() + ay - ax / 2));
        path.lineTo((float) dst.getX(), (float) dst.getY());
        path.lineTo((float) (dst.getX() + ax - ay / 2), (float) (dst.getY() + ay + ax / 2));
        return path;
    } else if (style == GraphConstants.ARROW_CIRCLE) {
        Ellipse2D ellipse = new Ellipse2D.Float((float) (dst.getX() + ax / 2 - size / 2), (float) (dst.getY() + ay / 2 - size / 2), size, size);
        dst.setLocation(dst.getX() + ax, dst.getY() + ay);
        return ellipse;
    } else if (style == GraphConstants.ARROW_LINE || style == GraphConstants.ARROW_DOUBLELINE) {
        GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO, 4);
        path.moveTo((float) (dst.getX() + ax / 2 + ay / 2), (float) (dst.getY() + ay / 2 - ax / 2));
        path.lineTo((float) (dst.getX() + ax / 2 - ay / 2), (float) (dst.getY() + ay / 2 + ax / 2));
        if (style == GraphConstants.ARROW_DOUBLELINE) {
            path.moveTo((float) (dst.getX() + ax / 3 + ay / 2), (float) (dst.getY() + ay / 3 - ax / 2));
            path.lineTo((float) (dst.getX() + ax / 3 - ay / 2), (float) (dst.getY() + ay / 3 + ax / 2));
        }
        return path;
    }
    return null;
}
