/**
	 * Sets the current model.
	 */
public void setModel(GraphModel model) {
    roots.clear();
    mapping.clear();
    hiddenMapping.clear();
    visibleSet.clear();
    graphModel = model;
    if (!isPartial()) {
        Object[] cells = DefaultGraphModel.getRoots(getModel());
        CellView[] cellViews = getMapping(cells, true);
        insertViews(cellViews);
    }
    // Update PortView Cache and Notify Observers 
    update();
}
