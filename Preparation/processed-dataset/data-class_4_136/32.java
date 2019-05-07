/******************************************************************************/
/**
 * Returns the Position of a Cell.
 * 
 * @param cell The cell, that holds the position of interest. 
 */
private Point2D.Double getPosition(CellView cell) {
    return (Point2D.Double) cell.getAttributes().get(KEY_POSITION);
}
