/**
	 * Removes the specified model root cells from the view by removing the
	 * mapping between the cell and its view and makes the cells invisible.
	 */
public CellView[] removeCells(Object[] cells) {
    if (cells != null && cells.length > 0) {
        CellView[] views = new CellView[cells.length];
        // Store views to be removed from roots in an intermediate 
        // set for performance reasons 
        Set removedRoots = null;
        for (int i = 0; i < cells.length; i++) {
            views[i] = removeMapping(cells[i]);
            if (views[i] != null) {
                views[i].removeFromParent();
                if (removedRoots == null) {
                    removedRoots = new HashSet();
                }
                removedRoots.add(views[i]);
                visibleSet.remove(views[i].getCell());
            }
        }
        if (removedRoots != null && removedRoots.size() > 0) {
            // If any roots have been removed, reform the roots 
            // lists appropriately, keeping the order the same 
            int newRootsSize = roots.size() - removedRoots.size();
            if (newRootsSize < 8) {
                newRootsSize = 8;
            }
            List newRoots = new ArrayList(newRootsSize);
            Iterator iter = roots.iterator();
            while (iter.hasNext()) {
                Object cell = iter.next();
                if (!removedRoots.contains(cell)) {
                    newRoots.add(cell);
                }
            }
            roots = newRoots;
        }
        return views;
    }
    return null;
}
