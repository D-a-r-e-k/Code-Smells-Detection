protected void cellWillCollapse(Object cell) {
    GraphModel model = getModel();
    if (movesParentsOnCollapse) {
        CellView view = getMapping(cell, false);
        if (view != null && !view.isLeaf()) {
            // Uses view-local attribute if available 
            AttributeMap attrs = view.getAttributes();
            if (!attrs.contains(GraphConstants.BOUNDS) && !localAttributes.contains(GraphConstants.BOUNDS))
                attrs = model.getAttributes(cell);
            // Moves the group to the origin of the children 
            Rectangle2D src = GraphConstants.getBounds(attrs);
            Rectangle2D b = view.getBounds();
            // FIXME: What if the group is exactly at "defaultBounds"? 
            if (resizesParentsOnCollapse || src == null || src.equals(VertexView.defaultBounds)) {
                src = attrs.createRect(b.getX(), b.getY(), b.getWidth() * collapseXScale, b.getHeight() * collapseYScale);
                attrs.applyValue(GraphConstants.BOUNDS, src);
            } else {
                src.setFrame(b.getX(), b.getY(), src.getWidth(), src.getHeight());
            }
        }
    }
}
