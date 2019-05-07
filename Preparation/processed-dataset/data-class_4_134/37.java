/******************************************************************************/
/**
 * When Cells are inserted and a update of the layout is desired, this method
 * defines the initial positions for all cells, the already layouted cells and
 * the inserted. The already layouted cells get their previos calculated 
 * position, gained from their bounds. The inserted Cells are positioned
 * recursivly. The inserted Cells, that have at least one relative in 
 * {@link #cellList} are placed in the barycenter of the relatives. After this,
 * the inserted Cells, with a new position are added to {@link #cellList}.
 * This is done, until all inserted Cells are in {@link #cellList}.
 * 
 * @param viewList List of the inserted Cells
 * @see #arrangeLayoutUpdateInsertedCellsPlacement(ArrayList)
 */
private void arrangeLayoutUpdateInsertPlacement(CellView[] viewList) {
    //preinitialisation - init positions for all known vertexViews 
    for (int i = 0; i < cellList.size(); i++) {
        CellView view = (CellView) cellList.get(i);
        if (!view.getAttributes().containsKey(KEY_POSITION)) {
            Point2D.Double pos = new Point2D.Double(view.getBounds().getCenterX(), view.getBounds().getCenterY());
            view.getAttributes().put(KEY_POSITION, pos);
        }
    }
    ArrayList placableCells = new ArrayList();
    for (int i = 0; i < viewList.length; i++) placableCells.add(viewList[i]);
    arrangeLayoutUpdateInsertedCellsPlacement(placableCells);
}
