/** Searches all Roots for the current Graph
	 *  First the method marks any Node as not visited.
	 *  Than calls searchRoots(MyGraphCell) for each
	 *  not visited Cell.
	 *  The Roots are stored in the Vector named roots
	 *
	 * 	@return returns a Vector with the roots
	 *  @see #searchRoots(JGraph, CellView[])
	 */
protected List searchRoots(JGraph jgraph, CellView[] selectedCellViews) {
    // get all cells and relations 
    List vertexViews = new ArrayList(selectedCellViews.length);
    List roots = new ArrayList();
    // first: mark all as not visited 
    // O(allCells&Edges) 
    for (int i = 0; i < selectedCellViews.length; i++) {
        if (selectedCellViews[i] instanceof VertexView) {
            VertexView vertexView = (VertexView) selectedCellViews[i];
            vertexView.getAttributes().remove(SUGIYAMA_VISITED);
            vertexViews.add(selectedCellViews[i]);
        }
    }
    // O(graphCells) 
    for (int i = 0; i < vertexViews.size(); i++) {
        VertexView vertexView = (VertexView) vertexViews.get(i);
        if (vertexView.getAttributes().get(SUGIYAMA_VISITED) == null) {
            searchRoots(jgraph, vertexView, roots);
        }
    }
    // Error Msg if the graph has no roots 
    if (roots.size() == 0) {
        throw new IllegalArgumentException("The Graph is not a DAG. Can't use Sugiyama Algorithm!");
    }
    return roots;
}
