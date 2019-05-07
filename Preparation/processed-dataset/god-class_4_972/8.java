/******************************************************************************/
/**
 * Sets the initial Values, gained from the {@link #config gpConfiguration} 
 * into the Cells.
 * 
 * @return Because the progress dialog is allready visible during the 
 * initialisation, <b><code>true</code><b> is returned when cancel is pressed
 * on it.
 */
private boolean initialize() {
    int length = cellList.size();
    for (int i = 0; i < length; i++) {
        CellView view = (CellView) cellList.get(i);
        initializeVertice(view);
    }
    for (int i = 0; i < applyCellList.size(); i++) computeLastImpulse((CellView) applyCellList.get(i));
    return false;
}
