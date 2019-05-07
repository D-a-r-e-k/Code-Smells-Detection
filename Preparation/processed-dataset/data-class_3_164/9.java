/**
	 * Sets up the layout in an initial positioning. All the first cells in each
	 * rank are moved to the left and the rest of the rank inserted as close
	 * together as their size and buffering permits. This method works on just
	 * the specified rank.
	 * 
	 * @param rankValue
	 *            the current rank being processed
	 * @param facade
	 *            the facade describing the input graph
	 * @param model
	 *            an internal model of the hierarchical layout
	 */
protected void rankCoordinates(int rankValue, JGraphFacade facade, JGraphHierarchyModel model) {
    JGraphHierarchyRank rank = (JGraphHierarchyRank) model.ranks.get(new Integer(rankValue));
    // Pad out the initial cell spacing to give a better chance of a cell 
    // not being restricted in one direction. 
    double extraCellSpacing = (widestRankValue - rankWidths[rankValue]) / (rank.size() + 1);
    double localIntraCellSpacing = intraCellSpacing + extraCellSpacing;
    // Check this doesn't make the rank too wide, if it does, reduce it 
    if (extraCellSpacing * (rank.size() + 1) + rankWidths[rankValue] > widestRankValue) {
        localIntraCellSpacing = intraCellSpacing;
    }
    double maxY = 0.0;
    double localX = initialX + extraCellSpacing;
    Iterator iter = rank.iterator();
    // Store whether or not any of the cells' bounds were unavailable so 
    // to only issue the warning once for all cells 
    boolean boundsWarning = false;
    while (iter.hasNext()) {
        JGraphAbstractHierarchyCell cell = (JGraphAbstractHierarchyCell) iter.next();
        if (cell.isVertex()) {
            JGraphHierarchyNode node = (JGraphHierarchyNode) cell;
            Rectangle2D bounds = facade.getBounds(node.cell);
            if (bounds != null) {
                if (orientation == SwingConstants.NORTH || orientation == SwingConstants.SOUTH) {
                    cell.width = bounds.getWidth();
                    cell.height = bounds.getHeight();
                } else {
                    cell.width = bounds.getHeight();
                    cell.height = bounds.getWidth();
                }
            } else {
                boundsWarning = true;
            }
            maxY = Math.max(maxY, cell.height);
        } else if (cell.isEdge()) {
            JGraphHierarchyEdge edge = (JGraphHierarchyEdge) cell;
            // The width is the number of additional parallel edges 
            // time the parallel edge spacing 
            int numEdges = 1;
            if (edge.edges != null) {
                numEdges = edge.edges.size();
            } else {
                logger.info("edge.edges is null");
            }
            cell.width = (numEdges - 1) * parallelEdgeSpacing;
        }
        // Set the initial x-value as being the best result so far 
        localX += cell.width / 2.0;
        cell.setX(rankValue, localX);
        cell.setGeneralPurposeVariable(rankValue, (int) localX);
        localX += cell.width / 2.0;
        localX += localIntraCellSpacing;
    }
    if (boundsWarning == true) {
        logger.info("At least one cell has no bounds");
    }
}
