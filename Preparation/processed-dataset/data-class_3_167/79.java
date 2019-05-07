/**
	 * Sends <code>cells</code> to back. Note: This expects an array of cells!
	 */
public void toBack(Object[] cells) {
    if (cells != null && cells.length > 0) {
        graphModel.toBack(cells);
    }
}
