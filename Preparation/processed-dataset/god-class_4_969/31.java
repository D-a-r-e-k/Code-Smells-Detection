/******************************************************************************/
/**
 * Returns the Position of a CellView
 * 
 * @param view CellView, the position is requested
 * @return Position of the CellView
 * @see #getAttribute(CellView,String)
 */
private Point2D.Double getPosition(CellView view) {
    return (Point2D.Double) getAttribute(view, KEY_POSITION);
}
