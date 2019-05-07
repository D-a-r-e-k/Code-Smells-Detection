/******************************************************************************/
/**
 * Returns the Position of a Cell contained in {@link #applyCellList}.
 * 
 * @param index Identifies the cell. This is the index of the cell in 
 * the given list of CellViews
 * @param list List containing only CellViews
 * @see #getAttribute(int,String,ArrayList)
 */
private Point2D.Double getPosition(int index, ArrayList list) {
    return (Point2D.Double) getAttribute(index, KEY_POSITION, list);
}
