/** Fills the Vector for the specified level with a wrapper
	 *  for the MyGraphCell. After that the method called for
	 *  each neighbor graph cell.
	 *
	 * @param jgraph
	 * @param levels
	 * @param level
	 * @param vertexView
	 */
protected void fillLevels(JGraph jgraph, List levels, int level, VertexView vertexView) {
    // precondition control 
    if (vertexView == null)
        return;
    // be sure that the list container exists for the current level 
    if (levels.size() == level)
        levels.add(level, new ArrayList());
    // if the cell already visited return 
    if (vertexView.getAttributes().get(SUGIYAMA_VISITED) != null) {
        // The graph is cyclic return. 
        return;
    }
    // is the cell already assigned to a level? 
    CellWrapper w = (CellWrapper) vertexView.getAttributes().get(SUGIYAMA_CELL_WRAPPER);
    if (w != null) {
        // is the current level OK? 
        if (w.getLevel() < level) {
            // The level is too high 
            //System.out.println("Problem with level:" + vertexView.getCell().toString() + " Current Level: " + w.getLevel() + " Parent Level:" + (level-1)); 
            // Remove the cell from the high level				 
            List listForTheHigherLevel = (ArrayList) levels.get(w.getLevel());
            listForTheHigherLevel.remove(w);
            vertexView.getAttributes().remove(SUGIYAMA_CELL_WRAPPER);
        } else {
            // the current assignment is OK, return 
            return;
        }
    }
    // mark as visited for cycle tests 
    vertexView.getAttributes().put(SUGIYAMA_VISITED, Boolean.TRUE);
    // put the current node into the current level 
    // get the Level list 
    List listForTheCurrentLevel = (ArrayList) levels.get(level);
    // Create a wrapper for the node 
    int numberForTheEntry = listForTheCurrentLevel.size();
    CellWrapper wrapper = new CellWrapper(level, numberForTheEntry, vertexView);
    // put the Wrapper in the LevelVector 
    listForTheCurrentLevel.add(wrapper);
    //		System.out.println(vertexView.getCell().toString() + " Level: " + level + " Nr: " + numberForTheEntry); 
    // concat the wrapper to the cell for an easy access 
    vertexView.getAttributes().put(SUGIYAMA_CELL_WRAPPER, wrapper);
    // if the Cell has no Ports we can return, there are no relations 
    Object vertex = vertexView.getCell();
    GraphModel model = jgraph.getModel();
    GraphLayoutCache cache = jgraph.getGraphLayoutCache();
    int portCount = model.getChildCount(vertex);
    // iterate any NodePort 
    for (int i = 0; i < portCount; i++) {
        Object port = model.getChild(vertex, i);
        // iterate any Edge in the port 
        Iterator itrEdges = model.edges(port);
        while (itrEdges.hasNext()) {
            Object edge = itrEdges.next();
            // if not selected, do not follow 
            if (!isSelected(cache, edge)) {
                continue;
            }
            // if the Edge is a forward edge we should follow this edge 
            if (port == model.getSource(edge)) {
                Object targetPort = model.getTarget(edge);
                Object targetVertex = model.getParent(targetPort);
                // if not selected, do not follow the vertex 
                if (!isSelected(cache, targetVertex)) {
                    continue;
                }
                VertexView targetVertexView = (VertexView) jgraph.getGraphLayoutCache().getMapping(targetVertex, false);
                fillLevels(jgraph, levels, (level + 1), targetVertexView);
            }
        }
    }
    if (listForTheCurrentLevel.size() > gridAreaSize) {
        gridAreaSize = listForTheCurrentLevel.size();
    }
    // unmark as visited for cycle tests 
    vertexView.getAttributes().remove(SUGIYAMA_VISITED);
}
