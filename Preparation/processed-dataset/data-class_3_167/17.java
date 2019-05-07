/**
	 * Returns the roots of the view.
	 */
public CellView[] getRoots() {
    CellView[] views = new CellView[roots.size()];
    roots.toArray(views);
    return views;
}
