/**
	 * Calculates the median position of the connected cell on the specified
	 * rank
	 * 
	 * @param connectedCells
	 *            the cells the candidate connects to on this level
	 * @param rankValue
	 *            the layer number of this rank
	 * @return the median rank order ( not x position ) of the connected cells
	 */
private int medianXValue(Object[] connectedCells, int rankValue) {
    if (connectedCells.length == 0) {
        return 0;
    }
    int[] medianValues = new int[connectedCells.length];
    for (int i = 0; i < connectedCells.length; i++) {
        medianValues[i] = ((JGraphAbstractHierarchyCell) connectedCells[i]).getGeneralPurposeVariable(rankValue);
    }
    Arrays.sort(medianValues);
    if (connectedCells.length % 2 == 1) {
        // For odd numbers of adjacent vertices return the median 
        return medianValues[connectedCells.length / 2];
    } else {
        int medianPoint = connectedCells.length / 2;
        int leftMedian = medianValues[medianPoint - 1];
        int rightMedian = medianValues[medianPoint];
        return ((leftMedian + rightMedian) / 2);
    }
}
