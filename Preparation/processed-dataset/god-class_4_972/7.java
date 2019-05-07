/******************************************************************************/
/**
 * Extracts the Cells from JGraph and fills {@link #applyCellList}, 
 * {@link #cellList} and {@link #edgeList}. If applyToAll is 
 * <b><code>false</code></b> only in jgraph selected cells are added to
 * {@link #applyCellList} else all cells are added.
 * 
 * @param jgraph actual instance of jgraph
 * cells or only on the selected. 
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
