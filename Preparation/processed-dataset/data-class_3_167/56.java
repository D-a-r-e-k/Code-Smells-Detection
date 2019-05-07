/**
	 * Inserts the <code>cells</code> and connections into the model, and
	 * absorbs the local attributes. This implementation sets the inserted cells
	 * visible and selects the new roots depending on graph.selectNewCells.
	 */
public void insert(Object[] roots, Map attributes, ConnectionSet cs, ParentMap pm, UndoableEdit[] e) {
    Object[] visible = null;
    if (isPartial() && showsInsertedCells) {
        List tmp = DefaultGraphModel.getDescendants(graphModel, roots);
        tmp.removeAll(visibleSet);
        if (!tmp.isEmpty())
            visible = tmp.toArray();
    }
    // Absorb local attributes 
    GraphLayoutCacheEdit edit = createLocalEdit(roots, attributes, visible, null);
    if (edit != null)
        e = augment(e, edit);
    graphModel.insert(roots, attributes, cs, pm, e);
}
