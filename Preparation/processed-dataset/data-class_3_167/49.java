/**
	 * The actual implementation of changing cells' visibility state. This
	 * method does not deal with creating the undo or updating the
	 * GraphLayoutCache correctly. The <code>setVisible</code> methods in this
	 * class are intended to be the main public way to change visiblilty.
	 * However, if you do not require the undo to be formed, this method is much
	 * quicker, just note that you must call <code>updatePorts</code> if this
	 * method returns true.
	 * 
	 * NOTE: Your GraphLayoutCache must be <code>partial</code> (set 
	 * <code>partial</code> to <code>true</code> in the constructor)
	 * in order to use the visibility functionality of expand/collapse,
	 * setVisible, etc.
	 * 
	 * @param cells
	 * @param visible
	 * @return whether or not the ports needed updating in the calling method
	 */
public boolean setVisibleImpl(Object[] cells, boolean visible) {
    cells = addVisibleDependencies(cells, visible);
    if (cells != null && isPartial()) {
        boolean updatePorts = false;
        // Update Visible Set 
        CellView[] views = new CellView[cells.length];
        if (!visible) {
            views = removeCells(cells);
        }
        // Set used for model roots contains call for performance 
        Set modelRoots = null;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] != null) {
                if (visible) {
                    visibleSet.add(cells[i]);
                    views[i] = getMapping(cells[i], true);
                } else {
                    if (views[i] != null) {
                        if (modelRoots == null) {
                            modelRoots = new HashSet(DefaultGraphModel.getRootsAsCollection(getModel()));
                        }
                        if (modelRoots.contains(views[i].getCell()) && remembersCellViews) {
                            hiddenMapping.put(views[i].getCell(), views[i]);
                        }
                        updatePorts = true;
                    }
                }
            }
        }
        // Make Cell Views Visible (if not already in place) 
        if (visible) {
            Set parentSet = new HashSet();
            Set removedRoots = null;
            for (int i = 0; i < views.length; i++) {
                if (views[i] != null) {
                    CellView view = views[i];
                    // Remove all children from roots 
                    CellView[] children = AbstractCellView.getDescendantViews(new CellView[] { view });
                    for (int j = 0; j < children.length; j++) {
                        if (removedRoots == null) {
                            removedRoots = new HashSet();
                        }
                        removedRoots.add(children[j]);
                    }
                    view.refresh(this, this, false);
                    // Link cellView into graphLayoutCache 
                    CellView parentView = view.getParentView();
                    if (parentView != null)
                        parentSet.add(parentView);
                    updatePorts = true;
                }
            }
            if (removedRoots != null && removedRoots.size() > 0) {
                // If any roots have been removed, reform the roots 
                // lists appropriately, keeping the order the same 
                List newRoots = new ArrayList();
                Iterator iter = roots.iterator();
                while (iter.hasNext()) {
                    Object cell = iter.next();
                    if (!removedRoots.contains(cell)) {
                        newRoots.add(cell);
                    }
                }
                roots = newRoots;
            }
            CellView[] parentViews = new CellView[parentSet.size()];
            parentSet.toArray(parentViews);
            refresh(parentViews, true);
        }
        return updatePorts;
    }
    return false;
}
