// 
// Cell Mapping 
// 
/**
	 * Takes an array of views and returns the array of the corresponding cells
	 * by using <code>getCell</code> for each view.
	 */
public Object[] getCells(CellView[] views) {
    if (views != null) {
        Object[] cells = new Object[views.length];
        for (int i = 0; i < views.length; i++) if (views[i] != null)
            cells[i] = views[i].getCell();
        return cells;
    }
    return null;
}
