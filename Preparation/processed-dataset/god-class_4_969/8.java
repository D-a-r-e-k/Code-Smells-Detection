/******************************************************************************/
/**
 * Extracts all cells, all edges and all cells, the algorithm should run for,
 * from JGraph. After calling this Method {@link #cellList}, 
 * {@link #applyCellList} and {@link #edgeList} is filled.
 * 
 * @param jgraph A instanz from JGraph, the Cells will be extract from.
 */
private void getNodes(JGraph jgraph, Object[] cells) {
    Object[] all = jgraph.getRoots();
    CellView[] view = jgraph.getGraphLayoutCache().getMapping(all, false);
    CellView[] selectedView = jgraph.getGraphLayoutCache().getMapping(cells, false);
    for (int i = 0; i < view.length; i++) if (view[i] instanceof VertexView) {
        cellList.add(view[i]);
        applyCellList.add(view[i]);
    } else if (view[i] instanceof EdgeView) {
        edgeList.add(view[i]);
    }
    for (int i = 0; i < selectedView.length; i++) if (selectedView[i] instanceof VertexView)
        applyCellList.add(selectedView[i]);
}
