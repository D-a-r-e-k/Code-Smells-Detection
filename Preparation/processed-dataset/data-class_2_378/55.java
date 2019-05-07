/**
     * Returns a cartesian point from a polar angle, length and bounding box
     *
     * @param bounds  the area inside which the point needs to be.
     * @param angle  the polar angle, in degrees.
     * @param length  the relative length. Given in percent of maximum extend.
     *
     * @return The cartesian point.
     */
protected Point2D getWebPoint(Rectangle2D bounds, double angle, double length) {
    double angrad = Math.toRadians(angle);
    double x = Math.cos(angrad) * length * bounds.getWidth() / 2;
    double y = -Math.sin(angrad) * length * bounds.getHeight() / 2;
    return new Point2D.Double(bounds.getX() + x + bounds.getWidth() / 2, bounds.getY() + y + bounds.getHeight() / 2);
}
