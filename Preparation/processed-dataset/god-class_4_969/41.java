/******************************************************************************/
/**
 * When a event reaches this method, it will be scanned, if there are
 * Cells removed or Cells inserted. When there are Cells removed from the graph,
 * they have to be removed from {@link #cellList}, {@link #edgeList} and from
 * {@link #applyCellList}. If there are Cells added, the layout update process
 * starts. This triggers the algorithm to try to find a suitable layout for
 * the inserted cells, by layouting them and some of the cells, available in
 * {@link #cellList}. The algorithm tries to stimulate the cells from 
 * {@link #cellList} to make place for the layout of the inserted Cells. 
 */
public void graphChanged(GraphModelEvent e) {
    if (!isRunning) {
        isRunning = true;
        Object[] vertexIns = e.getChange().getInserted();
        Object[] vertexRem = e.getChange().getRemoved();
        //Insert - Action 
        if (vertexIns != null && vertexRem == null) {
            if (vertexIns.length == 0) {
                isRunning = false;
                return;
            }
            CellView[] viewList = jgraph.getGraphLayoutCache().getMapping(vertexIns, false);
            if (viewList.length == 0) {
                isRunning = false;
                return;
            }
            applyCellList.clear();
            loadConfiguration(CONFIG_KEY_LAYOUT_UPDATE);
            //enables a workaround if a known bug is still present 
            boolean bugPresent = false;
            for (int i = 0; i < viewList.length; i++) if (viewList[i] == null) {
                bugPresent = true;
                break;
            }
            if (bugPresent)
                getAllEdges();
            arrangeLayoutUpdateInsertPlacement(viewList);
            getLayoutUpdateCells(viewList);
            if (applyCellList.size() == 0) {
                isRunning = false;
                return;
            }
            round = 0;
            if (isClusteringEnabled)
                clusterGraph();
            //algorithm start                 
            init(false);
            run();
            //algorithm end 
            if (isClusteringEnabled)
                declusterGraph();
            applyChanges();
            removeTemporaryData();
        } else if (vertexIns == null && vertexRem != null) {
            isRunning = true;
            CellView[] viewList = jgraph.getGraphLayoutCache().getMapping(vertexRem, false);
            for (int i = 0; i < viewList.length; i++) if (viewList[i] instanceof VertexView) {
                if (applyCellList.contains(viewList[i]))
                    applyCellList.remove(viewList[i]);
                if (cellList.contains(viewList[i]))
                    cellList.remove(viewList[i]);
            } else if (viewList[i] instanceof EdgeView) {
            }
        }
        isRunning = false;
    }
}
