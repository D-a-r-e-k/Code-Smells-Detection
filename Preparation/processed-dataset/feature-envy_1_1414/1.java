/**
	 * Takes each possible adjacent cell pair on each rank and checks if
	 * swapping them around reduces the number of crossing
	 * 
	 * @param mainLoopIteration
	 *            the iteration number of the main loop
	 * @param model
	 *            the internal model describing the hierarchy
	 */
private void transpose(int mainLoopIteration, JGraphHierarchyModel model) {
    boolean improved = true;
    // Track the number of iterations in case of looping 
    int count = 0;
    int maxCount = 10;
    while (improved && count++ < maxCount) {
        // On certain iterations allow allow swapping of cell pairs with 
        // equal edge crossings switched or not switched. This help to 
        // nudge a stuck layout into a lower crossing total. 
        boolean nudge = false;
        if (mainLoopIteration % 2 == 1 && count % 2 == 1) {
            nudge = true;
        }
        improved = false;
        for (int i = 0; i < model.ranks.size(); i++) {
            JGraphHierarchyRank rank = (JGraphHierarchyRank) model.ranks.get(new Integer(i));
            JGraphAbstractHierarchyCell[] orderedCells = new JGraphAbstractHierarchyCell[rank.size()];
            Iterator iter = rank.iterator();
            for (int j = 0; j < orderedCells.length; j++) {
                JGraphAbstractHierarchyCell cell = (JGraphAbstractHierarchyCell) iter.next();
                orderedCells[cell.getGeneralPurposeVariable(i)] = cell;
            }
            List leftCellAboveConnections = null;
            List leftCellBelowConnections = null;
            List rightCellAboveConnections = null;
            List rightCellBelowConnections = null;
            int[] leftAbovePositions = null;
            int[] leftBelowPositions = null;
            int[] rightAbovePositions = null;
            int[] rightBelowPositions = null;
            JGraphAbstractHierarchyCell leftCell = null;
            JGraphAbstractHierarchyCell rightCell = null;
            for (int j = 0; j < (rank.size() - 1); j++) {
                // For each intra-rank adjacent pair of cells 
                // see if swapping them around would reduce the 
                // number of edges crossing they cause in total 
                // On every cell pair except the first on each rank, we 
                // can save processing using the previous values for the 
                // right cell on the new left cell 
                if (j == 0) {
                    leftCell = orderedCells[j];
                    leftCellAboveConnections = leftCell.getNextLayerConnectedCells(i);
                    leftCellBelowConnections = leftCell.getPreviousLayerConnectedCells(i);
                    leftAbovePositions = new int[leftCellAboveConnections.size()];
                    leftBelowPositions = new int[leftCellBelowConnections.size()];
                    for (int k = 0; k < leftAbovePositions.length; k++) {
                        leftAbovePositions[k] = ((JGraphAbstractHierarchyCell) leftCellAboveConnections.get(k)).getGeneralPurposeVariable(i + 1);
                    }
                    for (int k = 0; k < leftBelowPositions.length; k++) {
                        leftBelowPositions[k] = ((JGraphAbstractHierarchyCell) leftCellBelowConnections.get(k)).getGeneralPurposeVariable(i - 1);
                    }
                } else {
                    leftCellAboveConnections = rightCellAboveConnections;
                    leftCellBelowConnections = rightCellBelowConnections;
                    leftAbovePositions = rightAbovePositions;
                    leftBelowPositions = rightBelowPositions;
                    leftCell = rightCell;
                }
                rightCell = orderedCells[j + 1];
                rightCellAboveConnections = rightCell.getNextLayerConnectedCells(i);
                rightCellBelowConnections = rightCell.getPreviousLayerConnectedCells(i);
                rightAbovePositions = new int[rightCellAboveConnections.size()];
                rightBelowPositions = new int[rightCellBelowConnections.size()];
                for (int k = 0; k < rightAbovePositions.length; k++) {
                    rightAbovePositions[k] = ((JGraphAbstractHierarchyCell) rightCellAboveConnections.get(k)).getGeneralPurposeVariable(i + 1);
                }
                for (int k = 0; k < rightBelowPositions.length; k++) {
                    rightBelowPositions[k] = ((JGraphAbstractHierarchyCell) rightCellBelowConnections.get(k)).getGeneralPurposeVariable(i - 1);
                }
                int totalCurrentCrossings = 0;
                int totalSwitchedCrossings = 0;
                for (int k = 0; k < leftAbovePositions.length; k++) {
                    for (int ik = 0; ik < rightAbovePositions.length; ik++) {
                        if (leftAbovePositions[k] > rightAbovePositions[ik]) {
                            totalCurrentCrossings++;
                        }
                        if (leftAbovePositions[k] < rightAbovePositions[ik]) {
                            totalSwitchedCrossings++;
                        }
                    }
                }
                for (int k = 0; k < leftBelowPositions.length; k++) {
                    for (int ik = 0; ik < rightBelowPositions.length; ik++) {
                        if (leftBelowPositions[k] > rightBelowPositions[ik]) {
                            totalCurrentCrossings++;
                        }
                        if (leftBelowPositions[k] < rightBelowPositions[ik]) {
                            totalSwitchedCrossings++;
                        }
                    }
                }
                if ((totalSwitchedCrossings < totalCurrentCrossings) || (totalSwitchedCrossings == totalCurrentCrossings && nudge)) {
                    int temp = leftCell.getGeneralPurposeVariable(i);
                    leftCell.setGeneralPurposeVariable(i, rightCell.getGeneralPurposeVariable(i));
                    rightCell.setGeneralPurposeVariable(i, temp);
                    // With this pair exchanged we have to switch all of 
                    // values for the left cell to the right cell so the 
                    // next iteration for this rank uses it as the left 
                    // cell again 
                    rightCellAboveConnections = leftCellAboveConnections;
                    rightCellBelowConnections = leftCellBelowConnections;
                    rightAbovePositions = leftAbovePositions;
                    rightBelowPositions = leftBelowPositions;
                    rightCell = leftCell;
                    if (!nudge) {
                        // Don't count nudges as improvement or we'll end 
                        // up stuck in two combinations and not finishing 
                        // as early as we should 
                        improved = true;
                    }
                }
            }
        }
    }
}
