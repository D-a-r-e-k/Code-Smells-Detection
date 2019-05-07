/******************************************************************************/
/**
 * Sets the position of a CellView to the given Position
 * 
 * @param view The CellView, the position should be set
 * @param x X-Coordinate of the new position
 * @param y Y-Coordinate of the new position
 * @see #setPosition(CellView,Point2D.Double)
 */
private void setPosition(CellView view, double x, double y) {
    setPosition(view, new Point2D.Double(x, y));
}
