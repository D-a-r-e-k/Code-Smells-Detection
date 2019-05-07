/** Method fills the levels and stores them in the member levels.
	
	 *  Each level was represended by a Vector with Cell Wrapper objects.
	 *  These Vectors are the elements in the <code>levels</code> Vector.
	 *
	 */
protected List fillLevels(JGraph jgraph, CellView[] selectedCellViews, List rootVertexViews) {
    List levels = new Vector();
    // mark as not visited 
    // O(allCells) 
    for (int i = 0; i < selectedCellViews.length; i++) {
        CellView cellView = selectedCellViews[i];
        // more stabile 
        if (cellView == null)
            continue;
        cellView.getAttributes().remove(SUGIYAMA_VISITED);
    }
    Iterator rootIter = rootVertexViews.iterator();
    while (rootIter.hasNext()) {
        VertexView vertexView = (VertexView) rootIter.next();
        fillLevels(jgraph, levels, 0, vertexView);
    }
    return levels;
}
