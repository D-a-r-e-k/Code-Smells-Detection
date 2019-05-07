/******************************************************************************/
/**
 * Removes the temporary Data from the Cells of the graph. During the run of the
 * Algorithm there has been plenty of Data stored in the Cells. These are
 * removed here, if the Algorithm is canceled or finished.
 */
private void removeTemporaryData() {
    for (int i = 0; i < applyCellList.size(); i++) ((CellView) applyCellList.get(i)).getAttributes().clear();
}
