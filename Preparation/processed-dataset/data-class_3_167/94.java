/**
	 * Returns all views, shortcut to getAllDescendants(getRoots())
	 */
public CellView[] getAllViews() {
    return getAllDescendants(getRoots());
}
