/**
	 * Adds the specified model root cells to the view. Do not add a view that
	 * is already in roots.
	 */
public void insertViews(CellView[] views) {
    if (views != null) {
        refresh(views, true);
        for (int i = 0; i < views.length; i++) {
            if (views[i] != null && getMapping(views[i].getCell(), false) != null) {
                CellView parentView = views[i].getParentView();
                Object parent = (parentView != null) ? parentView.getCell() : null;
                if (!graphModel.isPort(views[i].getCell()) && parent == null) {
                    roots.add(views[i]);
                }
            }
        }
    }
}
