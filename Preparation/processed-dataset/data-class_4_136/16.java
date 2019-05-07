/******************************************************************************/
/**
 * Subtracing two forces.
 * @param v1 Force, v2 should be subtracted from
 * @param v2 Force, that should be subtracted from v1.
 */
private Point2D.Double sub(Point2D.Double v1, Point2D.Double v2) {
    return new Point2D.Double(v1.getX() - v2.getX(), v1.getY() - v2.getY());
}
