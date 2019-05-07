/**
	 * Returns the view for the specified cell. If create is true and no view is
	 * found then a view is created using createView(Object).
	 */
public CellView getMapping(Object cell, boolean create) {
    if (cell == null)
        return null;
    CellView view = (CellView) mapping.get(cell);
    if (view == null && create && isVisible(cell)) {
        view = (CellView) hiddenMapping.get(cell);
        if (view != null) {
            putMapping(cell, view);
            hiddenMapping.remove(cell);
        } else {
            view = factory.createView(graphModel, cell);
            putMapping(cell, view);
            view.refresh(this, this, true);
            // Create Dependent 
            // Views 
            view.update(this);
        }
    }
    return view;
}
