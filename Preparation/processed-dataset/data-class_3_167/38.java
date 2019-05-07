/**
	 * Associates the specified model cell with the specified view.
	 */
public void putMapping(Object cell, CellView view) {
    if (cell != null && view != null)
        mapping.put(cell, view);
}
