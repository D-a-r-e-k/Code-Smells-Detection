/**
	 * Removes cells from the model, including all children and connected edges
	 * if <code>children</code> or <code>edges</code> is true, respectively.
	 * 
	 * @param cells
	 *            The cells to remove.
	 * @param descendants
	 *            Whether to remove all descendants as well.
	 * @param edges
	 *            Whether to remove all connected edges as well.
	 */
public void remove(Object[] cells, boolean descendants, boolean edges) {
    if (cells != null && cells.length > 0) {
        if (edges) {
            Object[] tmp = DefaultGraphModel.getEdges(getModel(), cells).toArray();
            Object[] newCells = new Object[cells.length + tmp.length];
            System.arraycopy(cells, 0, newCells, 0, cells.length);
            System.arraycopy(tmp, 0, newCells, cells.length, tmp.length);
            cells = newCells;
        }
        if (descendants)
            cells = DefaultGraphModel.getDescendants(getModel(), cells).toArray();
        remove(cells);
    }
}
