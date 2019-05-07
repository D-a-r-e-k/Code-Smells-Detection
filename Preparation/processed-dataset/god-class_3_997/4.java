/**
	 * Performs median minimisation over one rank.
	 * 
	 * @param rankValue
	 *            the layer number of this rank
	 * @param model
	 *            an internal model of the hierarchical layout
	 * @param nextRankValue
	 *            the layer number whose connected cels are to be laid out
	 *            relative to
	 */
protected void rankMedianPosition(int rankValue, JGraphHierarchyModel model, int nextRankValue) {
    JGraphHierarchyRank rankSet = (JGraphHierarchyRank) model.ranks.get(new Integer(rankValue));
    Object[] rank = rankSet.toArray();
    // Form an array of the order in which the cell are to be processed 
    // , the order is given by the weighted sum of the in or out edges, 
    // depending on whether we're travelling up or down the hierarchy. 
    WeightedCellSorter[] weightedValues = new WeightedCellSorter[rank.length];
    Map cellMap = new Hashtable(rank.length);
    for (int i = 0; i < rank.length; i++) {
        JGraphAbstractHierarchyCell currentCell = (JGraphAbstractHierarchyCell) rank[i];
        weightedValues[i] = new WeightedCellSorter();
        weightedValues[i].cell = currentCell;
        weightedValues[i].rankIndex = i;
        cellMap.put(currentCell, weightedValues[i]);
        Collection nextLayerConnectedCells = null;
        if (nextRankValue < rankValue) {
            nextLayerConnectedCells = currentCell.getPreviousLayerConnectedCells(rankValue);
        } else {
            nextLayerConnectedCells = currentCell.getNextLayerConnectedCells(rankValue);
        }
        // Calcuate the weighing based on this node type and those this 
        // node is connected to on the next layer 
        weightedValues[i].weightedValue = calculatedWeightedValue(currentCell, nextLayerConnectedCells);
    }
    Arrays.sort(weightedValues);
    // Set the new position of each node within the rank using 
    // its temp variable 
    for (int i = 0; i < weightedValues.length; i++) {
        int numConnectionsNextLevel = 0;
        JGraphAbstractHierarchyCell cell = weightedValues[i].cell;
        Object[] nextLayerConnectedCells = null;
        int medianNextLevel = 0;
        if (nextRankValue < rankValue) {
            nextLayerConnectedCells = cell.getPreviousLayerConnectedCells(rankValue).toArray();
        } else {
            nextLayerConnectedCells = cell.getNextLayerConnectedCells(rankValue).toArray();
        }
        if (nextLayerConnectedCells != null) {
            numConnectionsNextLevel = nextLayerConnectedCells.length;
            if (numConnectionsNextLevel > 0) {
                medianNextLevel = medianXValue(nextLayerConnectedCells, nextRankValue);
            } else {
                // For case of no connections on the next level set the 
                // median to be the current position and try to be 
                // positioned there 
                medianNextLevel = cell.getGeneralPurposeVariable(rankValue);
            }
        }
        double leftBuffer = 0.0;
        double leftLimit = -100000000.0;
        for (int j = weightedValues[i].rankIndex - 1; j >= 0; ) {
            WeightedCellSorter weightedValue = (WeightedCellSorter) cellMap.get(rank[j]);
            if (weightedValue != null) {
                JGraphAbstractHierarchyCell leftCell = weightedValue.cell;
                if (weightedValue.visited) {
                    // The left limit is the right hand limit of that 
                    // cell 
                    // plus any allowance for unallocated cells 
                    // in-between 
                    leftLimit = leftCell.getGeneralPurposeVariable(rankValue) + leftCell.width / 2.0 + intraCellSpacing + leftBuffer + cell.width / 2.0;
                    ;
                    j = -1;
                } else {
                    leftBuffer += leftCell.width + intraCellSpacing;
                    j--;
                }
            }
        }
        double rightBuffer = 0.0;
        double rightLimit = 100000000.0;
        for (int j = weightedValues[i].rankIndex + 1; j < weightedValues.length; ) {
            WeightedCellSorter weightedValue = (WeightedCellSorter) cellMap.get(rank[j]);
            if (weightedValue != null) {
                JGraphAbstractHierarchyCell rightCell = weightedValue.cell;
                if (weightedValue.visited) {
                    // The left limit is the right hand limit of that 
                    // cell 
                    // plus any allowance for unallocated cells 
                    // in-between 
                    rightLimit = rightCell.getGeneralPurposeVariable(rankValue) - rightCell.width / 2.0 - intraCellSpacing - rightBuffer - cell.width / 2.0;
                    j = weightedValues.length;
                } else {
                    rightBuffer += rightCell.width + intraCellSpacing;
                    j++;
                }
            }
        }
        if (medianNextLevel >= leftLimit && medianNextLevel <= rightLimit) {
            cell.setGeneralPurposeVariable(rankValue, (int) medianNextLevel);
        } else if (medianNextLevel < leftLimit) {
            // Couldn't place at median value, place as close to that 
            // value as possible 
            cell.setGeneralPurposeVariable(rankValue, (int) leftLimit);
            currentXDelta += leftLimit - medianNextLevel;
        } else if (medianNextLevel > rightLimit) {
            // Couldn't place at median value, place as close to that 
            // value as possible 
            cell.setGeneralPurposeVariable(rankValue, (int) rightLimit);
            currentXDelta += medianNextLevel - rightLimit;
        }
        weightedValues[i].visited = true;
    }
}
