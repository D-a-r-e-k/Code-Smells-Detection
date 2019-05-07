/**
	 * Returns a nested map of (cell, map) pairs that represent all attributes
	 * of all cell views in this view.
	 * 
	 * @see #getCellViews
	 */
public Map createNestedMap() {
    CellView[] cellViews = getCellViews();
    Map nested = new Hashtable();
    for (int i = 0; i < cellViews.length; i++) {
        nested.put(cellViews[i].getCell(), new Hashtable((Map) cellViews[i].getAllAttributes().clone()));
    }
    return nested;
}
