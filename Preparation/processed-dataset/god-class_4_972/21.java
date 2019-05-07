/******************************************************************************/
/**
 * Clears the temporary data from the cells in {@link #cellList} (all cells).
 */
private void removeTemporaryLayoutDataFromCells() {
    for (int i = 0; i < cellList.size(); i++) ((CellView) cellList.get(i)).getAttributes().clear();
}
