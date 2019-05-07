/**
	 * Removes the association for the specified model cell and returns the view
	 * that was previously associated with the cell. Updates the portlist if
	 * necessary.
	 */
public CellView removeMapping(Object cell) {
    if (cell != null) {
        CellView view = (CellView) mapping.remove(cell);
        return view;
    }
    return null;
}
