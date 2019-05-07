/******************************************************************************/
/**
 * Adding two forces.
 * @param v1 Force that should be added with v2
 * @param v2 Force that should be added with v1
 * @return Sum of both forces.
 */
private Point2D.Double add(Point2D.Double v1, Point2D.Double v2) {
    return new Point2D.Double(v1.getX() + v2.getX(), v1.getY() + v2.getY());
}
