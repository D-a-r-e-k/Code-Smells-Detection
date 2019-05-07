void method0() { 
/**
	 * The maximum number of iterations to perform whilst reducing edge
	 * crossings
	 */
protected int maxIterations = 24;
/**
	 * Stores each rank as a collection of cells in the best order found for
	 * each layer so far
	 */
protected Object[][] nestedBestRanks = null;
/**
	 * The total number of crossings found in the best configuration so far
	 */
protected int currentBestCrossings = 0;
protected int iterationsWithoutImprovement = 0;
protected int maxNoImprovementIterations = 2;
/**
	 * The layout progress bar
	 */
protected JGraphLayoutProgress progress = new JGraphLayoutProgress();
}
