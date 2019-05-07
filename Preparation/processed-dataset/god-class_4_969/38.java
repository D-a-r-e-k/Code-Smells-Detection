/******************************************************************************/
/**
 * Recursive method for finding the initial position for inserted cells. The
 * inserted cells are checked, whether there is at leased one of the relatives
 * in {@link #cellList}. If there is any, the cells are positioned in the
 * barycenter of the relatives. If there is only one relative, this means, the
 * inserted CellViews are positioned exactly on the position of the relative.
 * Cells with no relative in {@link #cellList} are stored in a list. After all
 * Cells are visited and checked, all positioned cells are added to 
 * {@link #cellList}. Then, while the list with the non positioned Cells is
 * not empty, the method is called recursivly again. This is done, until all
 * inserted cells are positioned or no relatives could be found for all left
 * Cells (that causes that the left cells are positioned in the upper left 
 * corner).
 * 
 * @param placableCells A List of CellViews, that have to be placed in the 
 * barycenter of their relatives
 * @see #arrangeLayoutUpdateInsertPlacement(CellView[])
 * @see #graphChanged(GraphModelEvent)  
 */
private void arrangeLayoutUpdateInsertedCellsPlacement(ArrayList placableCells) {
    ArrayList notPlacedCells = new ArrayList();
    for (int i = 0; i < placableCells.size(); i++) {
        CellView view = (CellView) placableCells.get(i);
        if (view instanceof VertexView) {
            ArrayList relatives = getRelativesFrom(cellList, view);
            if (relatives.size() != 0) {
                double sumX = 0.0;
                double sumY = 0.0;
                for (int j = 0; j < relatives.size(); j++) {
                    Point2D.Double pos = (Point2D.Double) ((CellView) relatives.get(j)).getAttributes().get(KEY_POSITION);
                    sumX += pos.x;
                    sumY += pos.y;
                }
                Point2D.Double randomVector = new Point2D.Double(Math.cos(Math.random() * 2.0 * Math.PI) * 10.0, Math.sin(Math.random() * 2.0 * Math.PI) * 10.0);
                view.getAttributes().put(KEY_POSITION, new Point2D.Double((sumX / relatives.size()) + randomVector.x, (sumY / relatives.size()) + randomVector.y));
            } else {
                notPlacedCells.add(view);
            }
        }
    }
    for (int i = 0; i < placableCells.size(); i++) {
        if (placableCells.get(i) != null)
            if (((CellView) placableCells.get(i)).getAttributes() != null)
                if (((CellView) placableCells.get(i)).getAttributes().containsKey(KEY_POSITION))
                    cellList.add(placableCells.get(i));
    }
    if (notPlacedCells.size() != placableCells.size()) {
        arrangeLayoutUpdateInsertedCellsPlacement(notPlacedCells);
    } else {
        for (int i = 0; i < notPlacedCells.size(); i++) {
            CellView view = (CellView) notPlacedCells.get(i);
            if (!view.getAttributes().containsKey(KEY_POSITION))
                view.getAttributes().put(KEY_POSITION, new Point2D.Double(0.0, 0.0));
        }
    }
    for (int i = 0; i < cellList.size(); i++) if (((CellView) cellList.get(i)).getAttributes().get(KEY_POSITION) == null)
        System.err.println("WHATCH OUT!!! NODE " + i + " == NULL");
}
