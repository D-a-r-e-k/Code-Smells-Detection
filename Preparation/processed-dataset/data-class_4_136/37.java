/******************************************************************************/
/**
 * Will be called, when cells are inserted or removed. When cells are removed,
 * they are also removed from {@link #cellList}, {@link #applyCellList} and
 * {@link #edgeList}. If cells are inserted a new layout update process starts.
 */
public void graphChanged(GraphModelEvent e) {
    if (!isRunning && isActive) {
        isRunning = true;
        GraphModelEvent.GraphModelChange change = e.getChange();
        Object[] objRem = change.getRemoved();
        Object[] objIns = change.getInserted();
        if (objRem == null && objIns != null) {
            // nodes inserted 
            for (int i = 0; i < cellList.size(); i++) initPosition((CellView) cellList.get(i));
            CellView[] viewInserted = jgraph.getGraphLayoutCache().getMapping(objIns, false);
            applyCellList = new ArrayList();
            /*extracting vertices into []*/
            int vertexViewCount = 0;
            for (int i = 0; i < viewInserted.length; i++) if (viewInserted[i] instanceof VertexView)
                vertexViewCount++;
            VertexView[] vertexList = new VertexView[vertexViewCount];
            vertexViewCount = 0;
            for (int i = 0; i < viewInserted.length; i++) if (viewInserted[i] instanceof VertexView)
                vertexList[vertexViewCount++] = (VertexView) viewInserted[i];
            /*extracting vertices into [] done*/
            //stops inserting process, if no vertex was inserted                 
            if (vertexList.length == 0) {
                isRunning = false;
                return;
            }
            //initialising runtime values from config 
            loadRuntimeValues(VALUES_INC);
            //the number of cells in applyCellList will probably change 
            sigmaRot /= 1.0 / applyCellList.size();
            //positioning the new nodes in the barycenter of old relatives                 
            arrangePlacement(vertexList);
            //add new vertices and some relatives to applyCellList 
            addApplyableVertices(vertexList);
            if (applyCellList.size() == 0) {
                isRunning = false;
                return;
            }
            //                showCellList(applyCellList,Color.GREEN); 
            if (isClusteringEnabled) {
                clusterGraph();
            }
            //the number of cells in applyCellList has changed probably 
            sigmaRot *= 1.0 / applyCellList.size();
            maxRounds = applyCellList.size() * 4;
            // performing algorithm on all nodes in applyCellList 
            initialize();
            calculate();
            // algorithm done 
            if (isClusteringEnabled)
                declusterGraph();
            if (useOptimizeAlgorithm)
                optimizationAlgorithm.run(jgraph, jgraph.getRoots(), null);
            //moves graph to the upper left corner 
            correctCoordinates();
            //taking changes    
            setNewCoordinates(jgraph);
            //removing algorithms attributes from nodes 
            removeTemporaryLayoutDataFromCells();
        } else if (objRem != null && objIns == null) {
            // nodes removed 
            CellView[] viewRemoved = jgraph.getGraphLayoutCache().getMapping(objRem, false);
            for (int i = 0; i < viewRemoved.length; i++) {
                if (viewRemoved[i] instanceof VertexView && cellList.contains(viewRemoved[i])) {
                    applyCellList.remove(viewRemoved[i]);
                    cellList.remove(viewRemoved[i]);
                }
            }
        }
        isRunning = false;
    }
}
