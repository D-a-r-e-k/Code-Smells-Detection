/**
	 * A basic horizontal coordinate assignment algorithm
	 * 
	 * @param facade
	 *            the facade describing the input graph
	 * @param model
	 *            an internal model of the hierarchical layout
	 * @return the updated hierarchy model
	 */
public JGraphHierarchyModel run(JGraphFacade facade, JGraphHierarchyModel model) {
    currentXDelta = 0.0;
    initialise(model);
    initialCoords(facade, model);
    if (fineTuning) {
        minNode(model);
    }
    double bestXDelta = 100000000.0;
    if (fineTuning) {
        for (int i = 0; i < maxIterations; i++) {
            // Median Heuristic 
            if (i != 0) {
                medianPos(i, model);
                minNode(model);
            }
            // if the total offset is less for the current positioning, 
            // there 
            // are less heavily angled edges and so the current positioning 
            // is used 
            if (currentXDelta < bestXDelta) {
                for (int j = 0; j < model.ranks.size(); j++) {
                    JGraphHierarchyRank rank = (JGraphHierarchyRank) model.ranks.get(new Integer(j));
                    Iterator iter = rank.iterator();
                    while (iter.hasNext()) {
                        JGraphAbstractHierarchyCell cell = (JGraphAbstractHierarchyCell) iter.next();
                        cell.setX(j, cell.getGeneralPurposeVariable(j));
                    }
                }
                bestXDelta = currentXDelta;
            } else {
                // Restore the best positions 
                for (int j = 0; j < model.ranks.size(); j++) {
                    JGraphHierarchyRank rank = (JGraphHierarchyRank) model.ranks.get(new Integer(j));
                    Iterator iter = rank.iterator();
                    while (iter.hasNext()) {
                        JGraphAbstractHierarchyCell cell = (JGraphAbstractHierarchyCell) iter.next();
                        cell.setGeneralPurposeVariable(j, (int) cell.getX(j));
                    }
                }
            }
            currentXDelta = 0;
        }
    }
    if (compactLayout) {
    }
    setCellLocations(facade, model);
    return model;
}
