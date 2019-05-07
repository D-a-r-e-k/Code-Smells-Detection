/**
	 * Calculates the priority the specified cell has based on the type of its
	 * cell and the cells it is connected to on the next layer
	 * 
	 * @param currentCell
	 *            the cell whose weight is to be calculated
	 * @param collection
	 *            the cells the specified cell is connected to
	 * @return the total weighted of the edges between these cells
	 */
private int calculatedWeightedValue(JGraphAbstractHierarchyCell currentCell, Collection collection) {
    int totalWeight = 0;
    Iterator iter = collection.iterator();
    while (iter.hasNext()) {
        JGraphAbstractHierarchyCell cell = (JGraphAbstractHierarchyCell) iter.next();
        if (currentCell.isVertex() && cell.isVertex()) {
            totalWeight++;
        } else if (currentCell.isEdge() && cell.isEdge()) {
            totalWeight += 8;
        } else {
            totalWeight += 2;
        }
    }
    return totalWeight;
}
