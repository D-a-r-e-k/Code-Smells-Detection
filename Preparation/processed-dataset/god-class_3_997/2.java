/**
	 * Performs one median positioning sweep in both directions
	 * 
	 * @param model
	 *            an internal model of the hierarchical layout
	 */
private void minNode(JGraphHierarchyModel model) {
    // Queue all nodes 
    LinkedList nodeList = new LinkedList();
    // Need to be able to map from cell to cellWrapper 
    Map map = new Hashtable();
    Object[][] rank = new Object[model.maxRank + 1][];
    for (int i = 0; i <= model.maxRank; i++) {
        JGraphHierarchyRank rankSet = (JGraphHierarchyRank) model.ranks.get(new Integer(i));
        rank[i] = rankSet.toArray();
        for (int j = 0; j < rank[i].length; j++) {
            // Use the weight to store the rank and visited to store whether 
            // or not the cell is in the list 
            JGraphAbstractHierarchyCell cell = (JGraphAbstractHierarchyCell) rank[i][j];
            WeightedCellSorter cellWrapper = new WeightedCellSorter(cell, i);
            cellWrapper.rankIndex = j;
            cellWrapper.visited = true;
            nodeList.add(cellWrapper);
            map.put(cell, cellWrapper);
        }
    }
    // Set a limit of the maximum number of times we will access the queue 
    // in case a loop appears 
    int maxTries = nodeList.size() * 10;
    int count = 0;
    // Don't move cell within this value of their median 
    int tolerance = 1;
    while (!nodeList.isEmpty() && count <= maxTries) {
        WeightedCellSorter cellWrapper = (WeightedCellSorter) nodeList.getFirst();
        JGraphAbstractHierarchyCell cell = cellWrapper.cell;
        int rankValue = cellWrapper.weightedValue;
        int rankIndex = cellWrapper.rankIndex;
        Object[] nextLayerConnectedCells = cell.getNextLayerConnectedCells(rankValue).toArray();
        Object[] previousLayerConnectedCells = cell.getPreviousLayerConnectedCells(rankValue).toArray();
        int numNextLayerConnected = nextLayerConnectedCells.length;
        int numPreviousLayerConnected = previousLayerConnectedCells.length;
        int medianNextLevel = medianXValue(nextLayerConnectedCells, rankValue + 1);
        int medianPreviousLevel = medianXValue(previousLayerConnectedCells, rankValue - 1);
        int numConnectedNeighbours = numNextLayerConnected + numPreviousLayerConnected;
        int currentPosition = cell.getGeneralPurposeVariable(rankValue);
        double cellMedian = currentPosition;
        if (numConnectedNeighbours > 0) {
            cellMedian = (medianNextLevel * numNextLayerConnected + medianPreviousLevel * numPreviousLayerConnected) / numConnectedNeighbours;
        }
        // Flag storing whether or not position has changed 
        boolean positionChanged = false;
        if (cellMedian < currentPosition - tolerance) {
            if (rankIndex == 0) {
                cell.setGeneralPurposeVariable(rankValue, (int) cellMedian);
                positionChanged = true;
            } else {
                JGraphAbstractHierarchyCell leftCell = (JGraphAbstractHierarchyCell) rank[rankValue][rankIndex - 1];
                int leftLimit = leftCell.getGeneralPurposeVariable(rankValue);
                leftLimit = leftLimit + (int) leftCell.width / 2 + (int) intraCellSpacing + (int) cell.width / 2;
                if (leftLimit < cellMedian) {
                    cell.setGeneralPurposeVariable(rankValue, (int) cellMedian);
                    positionChanged = true;
                } else if (leftLimit < cell.getGeneralPurposeVariable(rankValue) - tolerance) {
                    cell.setGeneralPurposeVariable(rankValue, leftLimit);
                    positionChanged = true;
                }
            }
        } else if (cellMedian > currentPosition + tolerance) {
            int rankSize = rank[rankValue].length;
            if (rankIndex == rankSize - 1) {
                cell.setGeneralPurposeVariable(rankValue, (int) cellMedian);
                positionChanged = true;
            } else {
                JGraphAbstractHierarchyCell rightCell = (JGraphAbstractHierarchyCell) rank[rankValue][rankIndex + 1];
                int rightLimit = rightCell.getGeneralPurposeVariable(rankValue);
                rightLimit = rightLimit - (int) rightCell.width / 2 - (int) intraCellSpacing - (int) cell.width / 2;
                if (rightLimit > cellMedian) {
                    cell.setGeneralPurposeVariable(rankValue, (int) cellMedian);
                    positionChanged = true;
                } else if (rightLimit > cell.getGeneralPurposeVariable(rankValue) + tolerance) {
                    cell.setGeneralPurposeVariable(rankValue, rightLimit);
                    positionChanged = true;
                }
            }
        }
        if (positionChanged) {
            // Add connected nodes to map and list 
            for (int i = 0; i < nextLayerConnectedCells.length; i++) {
                JGraphAbstractHierarchyCell connectedCell = (JGraphAbstractHierarchyCell) nextLayerConnectedCells[i];
                WeightedCellSorter connectedCellWrapper = (WeightedCellSorter) map.get(connectedCell);
                if (connectedCellWrapper != null) {
                    if (connectedCellWrapper.visited == false) {
                        connectedCellWrapper.visited = true;
                        nodeList.add(connectedCellWrapper);
                    }
                }
            }
            // Add connected nodes to map and list 
            for (int i = 0; i < previousLayerConnectedCells.length; i++) {
                JGraphAbstractHierarchyCell connectedCell = (JGraphAbstractHierarchyCell) previousLayerConnectedCells[i];
                WeightedCellSorter connectedCellWrapper = (WeightedCellSorter) map.get(connectedCell);
                if (connectedCellWrapper != null) {
                    if (connectedCellWrapper.visited == false) {
                        connectedCellWrapper.visited = true;
                        nodeList.add(connectedCellWrapper);
                    }
                }
            }
        }
        nodeList.removeFirst();
        cellWrapper.visited = false;
        count++;
    }
}
