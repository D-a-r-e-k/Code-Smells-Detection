/**
	 * Returns the views for the specified array of cells without creating these
	 * views on the fly.
	 */
public CellView[] getMapping(Object[] cells) {
    return getMapping(cells, false);
}
