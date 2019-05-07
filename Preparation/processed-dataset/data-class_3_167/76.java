/**
	 * Applies the <code>attributes</code> to all <code>cells</code> by
	 * creating a map that contains the attributes for each cell and passing it
	 * to edit on this layout cache. Example:
	 * 
	 * <pre>
	 * Map attrs = new java.util.Hashtable();
	 * GraphConstants.setBackground(attrs, Color.RED);
	 * graph.getGraphLayoutCache().edit(graph.getSelectionCells(), attrs);
	 * </pre>
	 */
public void edit(Object[] cells, Map attributes) {
    if (attributes != null && cells != null && cells.length > 0) {
        Map nested = new Hashtable();
        for (int i = 0; i < cells.length; i++) nested.put(cells[i], attributes);
        edit(nested, null, null, null);
    }
}
