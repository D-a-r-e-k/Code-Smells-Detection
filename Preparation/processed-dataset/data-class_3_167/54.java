/**
	 * Messaged when the user has altered the value for the item identified by
	 * cell to newValue. If newValue signifies a truly new value the model
	 * should post a graphCellsChanged event. This calls
	 * augmentNestedMapForValueChange.
	 */
public void valueForCellChanged(Object cell, Object newValue) {
    Map nested = null;
    if (isAutoSizeOnValueChange()) {
        CellView view = getMapping(cell, false);
        if (view != null) {
            AttributeMap attrs = view.getAllAttributes();
            Rectangle2D bounds = GraphConstants.getBounds(attrs);
            Rectangle2D dummyBounds = null;
            // Force the model to store the old bounds 
            if (bounds != null) {
                dummyBounds = attrs.createRect(bounds.getX(), bounds.getY(), 0, 0);
            } else {
                dummyBounds = attrs.createRect(0, 0, 0, 0);
            }
            nested = GraphConstants.createAttributes(new Object[] { cell }, new Object[] { GraphConstants.RESIZE, GraphConstants.BOUNDS }, new Object[] { Boolean.TRUE, dummyBounds });
        }
    } else {
        nested = new Hashtable();
        nested.put(cell, new Hashtable());
    }
    augmentNestedMapForValueChange(nested, cell, newValue);
    edit(nested, null, null, null);
}
