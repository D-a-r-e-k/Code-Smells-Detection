/**
	 * Performs one median positioning sweep in one direction
	 * 
	 * @param i
	 *            the iteration of the whole process
	 * @param model
	 *            an internal model of the hierarchical layout
	 */
private void medianPos(int i, JGraphHierarchyModel model) {
    // Reverse sweep direction each time through this method 
    boolean downwardSweep = (i % 2 == 0);
    if (downwardSweep) {
        for (int j = model.maxRank; j > 0; j--) {
            rankMedianPosition(j - 1, model, j);
        }
    } else {
        for (int j = 0; j < model.maxRank - 1; j++) {
            rankMedianPosition(j + 1, model, j);
        }
    }
}
