/**
	 * Called when a child has been made visible by expanding its parent. This
	 * implementation translates the child so that it reflects the offset of the
	 * parent group since the child was last visible (see
	 * {@link #movesChildrenOnExpand}).
	 */
protected void cellExpanded(Object cell) {
    GraphModel model = getModel();
    // Moves the child to the group origin if it is not a port 
    if (movesChildrenOnExpand && !model.isPort(cell)) {
        CellView view = getMapping(cell, false);
        if (view != null) {
            CellView parent = getMapping(model.getParent(cell), false);
            if (parent != null) {
                if (DefaultGraphModel.isVertex(model, parent)) {
                    // Computes the offset of the parent group 
                    Rectangle2D src = GraphConstants.getBounds(parent.getAllAttributes());
                    Rectangle2D rect = parent.getBounds();
                    if (rect != null && src != null) {
                        double dx = src.getX() - rect.getX();
                        double dy = src.getY() - rect.getY();
                        // Gets the attributes from the cell view or 
                        // cell and translates the bounds or points 
                        AttributeMap attrs = view.getAttributes();
                        if (!attrs.contains(GraphConstants.BOUNDS))
                            attrs = model.getAttributes(view.getCell());
                        attrs.translate(dx, dy);
                    }
                }
            }
        }
    }
}
