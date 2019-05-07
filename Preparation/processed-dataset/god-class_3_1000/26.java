// 
// Update View based on Model Change 
// 
/**
	 * Called from BasicGraphUI.ModelHandler to update the view based on the
	 * specified GraphModelEvent.
	 */
public void graphChanged(GraphModelEvent.GraphModelChange change) {
    // Get Old Attributes From GraphModelChange (Undo) -- used to remap 
    // removed cells 
    CellView[] views = change.getViews(this);
    if (views != null) {
        // Only ex-visible views are piggybacked 
        for (int i = 0; i < views.length; i++) if (views[i] != null) {
            // Do not use putMapping because cells are invisible 
            mapping.put(views[i].getCell(), views[i]);
        }
        // Ensure visible state 
        setVisibleImpl(getCells(views), true);
    }
    // Fetch View Order Of Changed Cells (Before any changes) 
    Object[] changed = change.getChanged();
    // Fetch Views to Insert before Removal (Special case: two step process, 
    // see setModel) 
    getMapping(change.getInserted(), true);
    // Remove and Hide Roots 
    views = removeCells(change.getRemoved());
    // Store Removed Attributes In GraphModelChange (Undo) 
    change.putViews(this, views);
    // Insert New Roots 
    // insertViews(insertViews); 
    // Hide edges with invisible source or target 
    if (isPartial()) {
        // Then show 
        showCellsForChange(change);
        // First hide 
        hideCellsForChange(change);
    }
    // Refresh Changed Cells 
    if (changed != null && changed.length > 0) {
        // Restore All Cells in Model Order (Replace Roots) 
        for (int i = 0; i < changed.length; i++) {
            CellView view = getMapping(changed[i], false);
            if (view != null) {
                view.refresh(this, this, true);
                // Update child edges in groups (routing) 
                update(view);
            }
        }
    }
    reloadRoots();
    // Refresh Context of Changed Cells (=Connected Edges) 
    refresh(getMapping(getContext(change), false), false);
    updatePorts();
}
