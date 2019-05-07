/**
	 * Sets up the layout in an initial positioning. The ranks are all centered
	 * as much as possible along the middle vertex in each rank. The other cells
	 * are then placed as close as possible on either side.
	 * 
	 * @param facade
	 *            the facade describing the input graph
	 * @param model
	 *            an internal model of the hierarchical layout
	 */
private void initialCoords(JGraphFacade facade, JGraphHierarchyModel model) {
    calculateWidestRank(facade, model);
    // Sweep up and down from the widest rank 
    for (int i = widestRank; i >= 0; i--) {
        if (i < model.maxRank) {
            rankCoordinates(i, facade, model);
        }
    }
    for (int i = widestRank + 1; i <= model.maxRank; i++) {
        if (i > 0) {
            rankCoordinates(i, facade, model);
        }
    }
}
