/**
	 * Inserts the cloned cells from the clone map and clones the passed-in
	 * arguments according to the clone map before insertion and returns the
	 * clones in order of the cells. This example shows how to clone the current
	 * selection and get a reference to the clones:
	 * 
	 * <pre>
	 * Object[] cells = graph.getDescendants(graph.order(graph.getSelectionCells()));
	 * ConnectionSet cs = ConnectionSet.create(graphModel, cells, false);
	 * ParentMap pm = ParentMap.create(graphModel, cells, false, true);
	 * cells = graphLayoutCache.insertClones(cells, graph.cloneCells(cells),
	 * 		attributes, cs, pm, 0, 0);
	 * </pre>
	 */
public Object[] insertClones(Object[] cells, Map clones, Map nested, ConnectionSet cs, ParentMap pm, double dx, double dy) {
    if (cells != null) {
        if (cs != null)
            cs = cs.clone(clones);
        if (pm != null)
            pm = pm.clone(clones);
        if (nested != null) {
            nested = GraphConstants.replaceKeys(clones, nested);
            AttributeMap.translate(nested.values(), dx, dy);
        }
        // Replace cells in order 
        Object[] newCells = new Object[cells.length];
        for (int i = 0; i < cells.length; i++) newCells[i] = clones.get(cells[i]);
        // Insert into cache/model 
        insert(newCells, nested, cs, pm, null);
        return newCells;
    }
    return null;
}
