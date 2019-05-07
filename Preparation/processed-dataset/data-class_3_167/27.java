/**
	 * Completely reloads all roots from the model in the order returned by
	 * DefaultGraphModel.getAll. This uses the current visibleSet and mapping to
	 * fetch the cell views for the cells.
	 */
protected void reloadRoots() {
    // Reorder roots 
    Object[] orderedCells = DefaultGraphModel.getAll(graphModel);
    List newRoots = new ArrayList();
    for (int i = 0; i < orderedCells.length; i++) {
        CellView view = getMapping(orderedCells[i], false);
        if (view != null) {
            view.refresh(this, this, true);
            if (view.getParentView() == null) {
                newRoots.add(view);
            }
        }
    }
    roots = newRoots;
}
