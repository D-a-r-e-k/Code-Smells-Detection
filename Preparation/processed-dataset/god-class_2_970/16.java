protected int moveToBarycenter(JGraph jgraph, List levels, int levelIndex) {
    // Counter for the movements 
    int movements = 0;
    // Get the current level 
    List currentLevel = (List) levels.get(levelIndex);
    GraphModel model = jgraph.getModel();
    GraphLayoutCache cache = jgraph.getGraphLayoutCache();
    for (int currentIndexInTheLevel = 0; currentIndexInTheLevel < currentLevel.size(); currentIndexInTheLevel++) {
        CellWrapper sourceWrapper = (CellWrapper) currentLevel.get(currentIndexInTheLevel);
        float gridPositionsSum = 0;
        float countNodes = 0;
        VertexView vertexView = sourceWrapper.getVertexView();
        Object vertex = vertexView.getCell();
        int portCount = model.getChildCount(vertex);
        for (int i = 0; i < portCount; i++) {
            Object port = model.getChild(vertex, i);
            Iterator edges = model.edges(port);
            while (edges.hasNext()) {
                Object edge = edges.next();
                // if edge not selected, do not follow 
                if (!isSelected(cache, edge))
                    continue;
                // if it is a forward edge follow it 
                Object neighborPort = null;
                if (port == model.getSource(edge)) {
                    neighborPort = model.getTarget(edge);
                } else {
                    if (port == model.getTarget(edge)) {
                        neighborPort = model.getSource(edge);
                    } else {
                        continue;
                    }
                }
                Object neighborVertex = model.getParent(neighborPort);
                // if vertex not selected, do not follow 
                if (!isSelected(cache, neighborVertex))
                    continue;
                VertexView neighborVertexView = (VertexView) jgraph.getGraphLayoutCache().getMapping(neighborVertex, false);
                if (neighborVertexView == null)
                    continue;
                CellWrapper targetWrapper = (CellWrapper) neighborVertexView.getAttributes().get(SUGIYAMA_CELL_WRAPPER);
                if (targetWrapper == sourceWrapper)
                    continue;
                if (targetWrapper == null || targetWrapper.getLevel() == levelIndex)
                    continue;
                gridPositionsSum += targetWrapper.getGridPosition();
                countNodes++;
            }
        }
        //---------------------------------------------------------- 
        // move node to new x coord 
        //---------------------------------------------------------- 
        if (countNodes > 0) {
            float tmp = (gridPositionsSum / countNodes);
            int newGridPosition = Math.round(tmp);
            boolean toRight = (newGridPosition > sourceWrapper.getGridPosition());
            boolean moved = true;
            while (newGridPosition != sourceWrapper.getGridPosition() && moved) {
                moved = move(toRight, currentLevel, currentIndexInTheLevel, sourceWrapper.getPriority());
                if (moved)
                    movements++;
            }
        }
    }
    return movements;
}
