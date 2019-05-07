/**
	 * Returns the views for the specified array of cells. Returned array may
	 * contain null pointers if the respective cell is not mapped in this view
	 * and <code>create</code> is <code>false</code>.
	 */
public CellView[] getMapping(Object[] cells, boolean create) {
    if (cells != null) {
        CellView[] result = new CellView[cells.length];
        for (int i = 0; i < cells.length; i++) result[i] = getMapping(cells[i], create);
        return result;
    }
    return null;
}
