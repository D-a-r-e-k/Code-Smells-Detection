protected void moveToBarycenter(JGraph jgraph, CellView[] allSelectedViews, List levels) {
    //================================================================ 
    // iterate any ReViewNodePort 
    GraphModel model = jgraph.getModel();
    GraphLayoutCache cache = jgraph.getGraphLayoutCache();
    for (int i = 0; i < allSelectedViews.length; i++) {
        if (!(allSelectedViews[i] instanceof VertexView))
            continue;
        VertexView vertexView = (VertexView) allSelectedViews[i];
        CellWrapper currentwrapper = (CellWrapper) vertexView.getAttributes().get(SUGIYAMA_CELL_WRAPPER);
        Object vertex = vertexView.getCell();
        int portCount = model.getChildCount(vertex);
        for (int k = 0; k < portCount; k++) {
            Object port = model.getChild(vertex, k);
            // iterate any Edge in the port 
            Iterator edges = model.edges(port);
            while (edges.hasNext()) {
                Object edge = edges.next();
                // if edge not selected, do not follow 
                if (!isSelected(cache, edge))
                    continue;
                Object neighborPort = null;
                // if the Edge is a forward edge we should follow this edge 
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
                if (neighborVertexView == null || neighborVertexView == vertexView)
                    continue;
                CellWrapper neighborWrapper = (CellWrapper) neighborVertexView.getAttributes().get(SUGIYAMA_CELL_WRAPPER);
                if (currentwrapper == null || neighborWrapper == null || currentwrapper.level == neighborWrapper.level)
                    continue;
                currentwrapper.priority++;
            }
        }
    }
    //================================================================ 
    for (Iterator levelsIter = levels.iterator(); levelsIter.hasNext(); ) {
        List level = (List) levelsIter.next();
        int i = 0;
        for (Iterator levelIter = level.iterator(); levelIter.hasNext(); i++) {
            // calculate the initial Grid Positions 1, 2, 3, .... per Level 
            CellWrapper wrapper = (CellWrapper) levelIter.next();
            wrapper.setGridPosition(i);
        }
    }
    movements.clear();
    movementsCurrentLoop = -1;
    movementsMax = Integer.MIN_VALUE;
    iteration = 0;
    //int movements = 1; 
    while (movementsCurrentLoop != 0) {
        // reset movements 
        movementsCurrentLoop = 0;
        // top down 
        for (int i = 1; i < levels.size(); i++) {
            movementsCurrentLoop += moveToBarycenter(jgraph, levels, i);
        }
        // bottom up 
        for (int i = levels.size() - 1; i >= 0; i--) {
            movementsCurrentLoop += moveToBarycenter(jgraph, levels, i);
        }
        this.updateProgress4Movements();
    }
}
