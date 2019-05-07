/**
	 * Returns the incoming or outgoing edges for cell. Cell should be a port or
	 * a vertex.
	 * 
	 * @param cell
	 *            The cell from which the edges will be determined
	 * @param exclude
	 *            The set of edges to ignore when searching
	 * @param visibleCells
	 *            whether or not only visible cells should be processed
	 * @param selfLoops
	 *            whether or not to include self loops in the returned list
	 * @param incoming
	 *            <code>true</code> if incoming edges are to be obtained,
	 *            <code>false</code> if outgoing edges are to be obtained
	 * @return Returns the list of incoming or outgoing edges for
	 *         <code>cell</code>
	 */
protected List getEdges(Object cell, Set exclude, boolean visibleCells, boolean selfLoops, boolean incoming) {
    GraphModel model = getModel();
    Object[] edges = DefaultGraphModel.getEdges(model, cell, incoming);
    List edgeList = new ArrayList(edges.length);
    Set localExclude = new HashSet(edges.length);
    for (int i = 0; i < edges.length; i++) {
        // Check that the edge is neiter in the passed in exclude set or 
        // the local exclude set. Also, if visibleCells is true check 
        // the edge is visible in the cache. 
        if ((exclude == null || !exclude.contains(edges[i])) && !localExclude.contains(edges[i]) && (!visibleCells || isVisible(edges[i]))) {
            // Add the edge to the list if all edges, including self loops 
            // are allowed. If self loops are not allowed, ensure the 
            // source and target of the edge are different 
            if (selfLoops == true || model.getSource(edges[i]) != model.getTarget(edges[i])) {
                edgeList.add(edges[i]);
            }
            localExclude.add(edges[i]);
        }
    }
    return edgeList;
}
