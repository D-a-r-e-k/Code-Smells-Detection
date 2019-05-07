/**
	 * A helper method to return various arrays of cells that are visible in
	 * this cache. For example, to get all selected vertices in a graph, do
	 * <code>graph.getSelectionCells(graph.getGraphLayoutCache().getCells(false, true,
	 false, false));</code>
	 */
public Object[] getCells(boolean groups, boolean vertices, boolean ports, boolean edges) {
    CellView[] views = getCellViews();
    List result = new ArrayList(views.length);
    GraphModel model = getModel();
    for (int i = 0; i < views.length; i++) {
        Object cell = views[i].getCell();
        boolean isEdge = model.isEdge(cell);
        if ((ports || !model.isPort(cell))) {
            if (((((ports || vertices) && !isEdge) || (edges && isEdge)) && views[i].isLeaf()) || (groups && !views[i].isLeaf()))
                result.add(views[i].getCell());
        }
    }
    return result.toArray();
}
