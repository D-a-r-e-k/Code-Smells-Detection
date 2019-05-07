/**
	 * Toggles the collapsed state of the specified cells.
	 * NOTE: Your GraphLayoutCache must be <code>partial</code> (set 
	 * <code>partial</code> to <code>true</code> in the constructor)
	 * in order to use the visibility functionality of expand/collapse,
	 * setVisible, etc.
	 * 
	 * @param cells
	 *            The cells to toggle the collapsed state for.
	 * @param collapseOnly
	 *            Whether cells should only be collapsed.
	 * @param expandOnly
	 *            Whether cells should only be expanded.
	 * 
	 */
public void toggleCollapsedState(Object[] cells, boolean collapseOnly, boolean expandOnly) {
    List toExpand = new ArrayList();
    List toCollapse = new ArrayList();
    for (int i = 0; i < cells.length; i++) {
        Object cell = cells[i];
        CellView view = getMapping(cell, false);
        if (view != null) {
            // Adds to list of expansion cells if it is a leaf in the layout 
            // cache and we do not only want to collapse. 
            if (view.isLeaf() && !collapseOnly)
                toExpand.add(view.getCell());
            else if (!view.isLeaf() && !expandOnly)
                toCollapse.add(view.getCell());
        }
    }
    if (!toCollapse.isEmpty() || !toExpand.isEmpty())
        setCollapsedState(toCollapse.toArray(), toExpand.toArray());
}
