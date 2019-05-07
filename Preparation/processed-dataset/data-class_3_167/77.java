/**
	 * Applies the <code>attributes</code> to a single <code>cell</code> by
	 * creating a map that contains the attributes for this cell and passing it
	 * to edit on this layout cache. Example:
	 * 
	 * <pre>
	 * Map attrs = new java.util.Hashtable();
	 * GraphConstants.setBackground(attrs, Color.RED);
	 * graph.getGraphLayoutCache().editCell(graph.getSelectionCell(), attrs);
	 * </pre>
	 */
public void editCell(Object cell, Map attributes) {
    if (attributes != null && cell != null) {
        edit(new Object[] { cell }, attributes);
    }
}
