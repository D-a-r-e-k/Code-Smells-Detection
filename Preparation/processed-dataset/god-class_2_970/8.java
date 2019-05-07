/** Searches Roots for the current Cell.
	 *
	 *  Therefore he looks at all Ports from the Cell.
	 *  At the Ports he looks for Edges.
	 *  At the Edges he looks for the Target.
	 *  If the Ports of the current Cell contains the target ReViewNodePort
	 *  he follows the edge to the source and looks at the
	 *  Cell for this source.
	 *  
	 * @param jgraph
	 * @param vertexViewToInspect
	 * @param roots
	 */
protected void searchRoots(JGraph jgraph, VertexView vertexViewToInspect, List roots) {
    // the node already visited 
    if (vertexViewToInspect.getAttributes().get(SUGIYAMA_VISITED) != null) {
        return;
    }
    // mark as visited for cycle tests 
    vertexViewToInspect.getAttributes().put(SUGIYAMA_VISITED, Boolean.TRUE);
    GraphModel model = jgraph.getModel();
    GraphLayoutCache cache = jgraph.getGraphLayoutCache();
    // get all Ports and search the relations at the ports 
    //List vertexPortViewList = new ArrayList() ; 
    Object vertex = vertexViewToInspect.getCell();
    int portCount = model.getChildCount(vertex);
    for (int j = 0; j < portCount; j++) {
        Object port = model.getChild(vertex, j);
        // Test all relations for where 
        // the current node is a target node 
        // for roots 
        boolean isRoot = true;
        Iterator itrEdges = model.edges(port);
        while (itrEdges.hasNext()) {
            Object edge = itrEdges.next();
            // if not selected do not follow 
            if (!isSelected(cache, edge)) {
                continue;
            }
            // if the current node is a target node 
            // get the source node and test 
            // the source node for roots 
            if (model.getTarget(edge) == port) {
                Object sourcePort = model.getSource(edge);
                Object sourceVertex = model.getParent(sourcePort);
                CellView sourceVertexView = jgraph.getGraphLayoutCache().getMapping(sourceVertex, false);
                if (sourceVertexView instanceof VertexView) {
                    searchRoots(jgraph, (VertexView) sourceVertexView, roots);
                    isRoot = false;
                }
            }
        }
        // The current node is never a Target Node 
        // -> The current node is a root node 
        if (isRoot) {
            roots.add(vertexViewToInspect);
        }
    }
}
