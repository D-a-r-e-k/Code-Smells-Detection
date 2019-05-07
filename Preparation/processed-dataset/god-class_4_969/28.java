/******************************************************************************/
/**
 * Sets the position of a CellView to the given Position
 * 
 * @param view The CellView, the position should be set
 * @param pos New Position
 * @see #setAttribute(CellView,String,Object)
 */
private void setPosition(CellView view, Point2D.Double pos) {
    setAttribute(view, KEY_POSITION, pos);
}
