/******************************************************************************/
/**
 * Sets the position of a CellView member of {@link #applyCellList} to the given
 * position.
 * 
 * @param index ID of the CellView in {@link #applyCellList}
 * @param x X-Coordinate of the new position
 * @param y Y-Coordinate of the new position
 * @see #setPosition(CellView,double,double)
 */
private void setPosition(int index, double x, double y) {
    setPosition((CellView) applyCellList.get(index), x, y);
}
