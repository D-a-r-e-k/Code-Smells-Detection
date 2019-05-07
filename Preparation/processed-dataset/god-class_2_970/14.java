/**
	 *  @return movements
	 */
protected int solveEdgeCrosses(JGraph jgraph, boolean down, List levels, int levelIndex) {
    // Get the current level 
    List currentLevel = (List) levels.get(levelIndex);
    int movements = 0;
    // restore the old sort 
    Object[] levelSortBefore = currentLevel.toArray();
    // new sort 
    Collections.sort(currentLevel);
    // test for movements 
    for (int j = 0; j < levelSortBefore.length; j++) {
        if (((CellWrapper) levelSortBefore[j]).getEdgeCrossesIndicator() != ((CellWrapper) currentLevel.get(j)).getEdgeCrossesIndicator()) {
            movements++;
        }
    }
    GraphModel model = jgraph.getModel();
    GraphLayoutCache cache = jgraph.getGraphLayoutCache();
    // Colections Sort sorts the highest value to the first value 
    for (int j = currentLevel.size() - 1; j >= 0; j--) {
        CellWrapper sourceWrapper = (CellWrapper) currentLevel.get(j);
        VertexView sourceView = sourceWrapper.getVertexView();
        Object sourceVertex = sourceView.getCell();
        int sourcePortCount = model.getChildCount(sourceVertex);
        for (int k = 0; k < sourcePortCount; k++) {
            Object sourcePort = model.getChild(sourceVertex, k);
            Iterator sourceEdges = model.edges(sourcePort);
            while (sourceEdges.hasNext()) {
                Object edge = sourceEdges.next();
                // if not selected, do not follow 
                if (!isSelected(cache, edge)) {
                    continue;
                }
                // if it is a forward edge follow it 
                Object targetPort = null;
                if (down && sourcePort == model.getSource(edge)) {
                    targetPort = model.getTarget(edge);
                }
                if (!down && sourcePort == model.getTarget(edge)) {
                    targetPort = model.getSource(edge);
                }
                if (targetPort == null)
                    continue;
                Object targetCell = model.getParent(targetPort);
                // if the target cell not selected, do not follow 
                if (!isSelected(cache, targetCell))
                    continue;
                VertexView targetVertexView = (VertexView) jgraph.getGraphLayoutCache().getMapping(targetCell, false);
                if (targetVertexView == null)
                    continue;
                CellWrapper targetWrapper = (CellWrapper) targetVertexView.getAttributes().get(SUGIYAMA_CELL_WRAPPER);
                // do it only if the edge is a forward edge to a deeper level 
                if (down && targetWrapper != null && targetWrapper.getLevel() > levelIndex) {
                    targetWrapper.addToEdgeCrossesIndicator(sourceWrapper.getEdgeCrossesIndicator());
                }
                if (!down && targetWrapper != null && targetWrapper.getLevel() < levelIndex) {
                    targetWrapper.addToEdgeCrossesIndicator(sourceWrapper.getEdgeCrossesIndicator());
                }
            }
        }
    }
    return movements;
}
