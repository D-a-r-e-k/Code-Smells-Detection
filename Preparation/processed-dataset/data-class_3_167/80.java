/**
	 * Brings <code>cells</code> to front. Note: This expects an array of
	 * cells!
	 */
public void toFront(Object[] cells) {
    if (cells != null && cells.length > 0) {
        graphModel.toFront(cells);
    }
}
