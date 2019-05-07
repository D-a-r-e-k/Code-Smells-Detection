/**
	 * Implementation.
	 *
	 * First of all the Algorithm searches the roots from the
	 * Graph. Starting from this roots the Algorithm creates
	 * levels and stores them in the member <code>levels</code>.
	 * The Member levels contains Vector Objects and the Vector per level
	 * contains Cell Wrapper Objects. After that the Algorithm
	 * tries to solve the edge crosses from level to level and
	 * goes top down and bottom up. After minimization of the
	 * edge crosses the algorithm moves each node to its
	 * bary center. Last but not Least the method draws the Graph.
	 *
	 *
     * @param graph JGraph instance
     * @param dynamic_cells List of all nodes the layout should move
     * @param static_cells List of node the layout should not move but allow for
	 */
public void run(JGraph graph, Object[] dynamic_cells, Object[] static_cells) {
    CellView[] selectedCellViews = graph.getGraphLayoutCache().getMapping(dynamic_cells);
    // gridAreaSize should really belong to the run state. 
    gridAreaSize = Integer.MIN_VALUE;
    /*  The Algorithm distributes the nodes on a grid.
		 *  For this grid you can configure the horizontal spacing.
		 *  This field specifies the configured value
		 *
		 */
    Rectangle2D maxBounds = new Rectangle2D.Double();
    for (int i = 0; i < selectedCellViews.length; i++) {
        // Add vertex to list 
        if (selectedCellViews[i] instanceof VertexView) {
            // Fetch Bounds 
            Rectangle2D bounds = selectedCellViews[i].getBounds();
            // Update Maximum 
            if (bounds != null)
                maxBounds.setFrame(0, 0, Math.max(bounds.getWidth(), maxBounds.getWidth()), Math.max(bounds.getHeight(), maxBounds.getHeight()));
        }
    }
    if (spacing.x == 0)
        spacing.x = (int) (2 * maxBounds.getWidth());
    if (spacing.y == 0)
        spacing.y = (int) (2 * maxBounds.getHeight());
    // (jgraph.getGridSize()*6); 
    // mark selected cell views in the graph 
    markSelected(selectedCellViews, true);
    // search all roots 
    List roots = searchRoots(graph, selectedCellViews);
    // return if no root found 
    if (roots.size() == 0)
        return;
    // create levels 
    List levels = fillLevels(graph, selectedCellViews, roots);
    // solves the edge crosses 
    solveEdgeCrosses(graph, levels);
    // move all nodes into the barycenter 
    moveToBarycenter(graph, selectedCellViews, levels);
    Point min = flushToOrigin ? new Point(0, 0) : findMinimumAndSpacing(selectedCellViews, spacing);
    // draw the graph in the window 
    drawGraph(graph, levels, min, spacing);
    // remove marks from the selected cell views in the graph 
    markSelected(selectedCellViews, false);
}
