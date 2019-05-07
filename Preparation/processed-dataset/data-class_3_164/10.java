/**
	 * Calculates the width rank in the hierarchy. Also set the y value of each
	 * rank whilst performing the calculation
	 * 
	 * @param facade
	 *            the facade describing the input graph
	 * @param model
	 *            an internal model of the hierarchical layout
	 */
protected void calculateWidestRank(JGraphFacade facade, JGraphHierarchyModel model) {
    // Starting y co-ordinate 
    double y = -interRankCellSpacing;
    // Track the widest cell on the last rank since the y 
    // difference depends on it 
    double lastRankMaxCellHeight = 0.0;
    rankWidths = new double[model.maxRank + 1];
    rankY = new double[model.maxRank + 1];
    for (int rankValue = model.maxRank; rankValue >= 0; rankValue--) {
        // Keep track of the widest cell on this rank 
        double maxCellHeight = 0.0;
        JGraphHierarchyRank rank = (JGraphHierarchyRank) model.ranks.get(new Integer(rankValue));
        double localX = initialX;
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
                maxCellHeight = Math.max(maxCellHeight, cell.height);
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
            localX += intraCellSpacing;
            if (localX > widestRankValue) {
                widestRankValue = localX;
                widestRank = rankValue;
            }
            rankWidths[rankValue] = localX;
        }
        if (boundsWarning == true) {
            logger.info("At least one cell has no bounds");
        }
        rankY[rankValue] = y;
        double distanceToNextRank = maxCellHeight / 2.0 + lastRankMaxCellHeight / 2.0 + interRankCellSpacing;
        lastRankMaxCellHeight = maxCellHeight;
        if (orientation == SwingConstants.NORTH || orientation == SwingConstants.WEST) {
            y += distanceToNextRank;
        } else {
            y -= distanceToNextRank;
        }
        iter = rank.iterator();
        while (iter.hasNext()) {
            JGraphAbstractHierarchyCell cell = (JGraphAbstractHierarchyCell) iter.next();
            cell.setY(rankValue, y);
        }
    }
}
