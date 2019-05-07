/**
	 * Returns a an array with the visible cells in <code>cells</code>.
	 */
public Object[] getVisibleCells(Object[] cells) {
    if (cells != null) {
        List result = new ArrayList(cells.length);
        for (int i = 0; i < cells.length; i++) if (isVisible(cells[i]))
            result.add(cells[i]);
        return result.toArray();
    }
    return null;
}
