/******************************************************************************/
/**
     * Starts the Calculation of a new layout with the GEM-Algorithm
     * @param graph JGraph instance
     * @param dynamic_cells List of all nodes the layout should move
     * @param static_cells List of node the layout should not move but allow for
     * @see #initialize()
     * @see #calculate()
	 */
public void run(JGraph graph, Object[] dynamic_cells, Object[] static_cells) {
    isRunning = true;
    jgraph = graph;
    jgraph.getModel().addGraphModelListener(this);
    cellList = new ArrayList();
    applyCellList = new ArrayList();
    //extracting the nodes from jgraph, the algorithm should be performed on 
    getNodes(jgraph, dynamic_cells);
    //        long starttime = System.currentTimeMillis(); 
    //ALGORITHM START 
    boolean isCanceled = initialize();
    // initializes algorithm 
    // sets the startvalues in cells 
    if (!isCanceled)
        isCanceled = calculate();
    //performs the algorithm on the cells 
    //ALGORITHM END 
    if (!isCanceled && useOptimizeAlgorithm)
        optimizationAlgorithm.performOptimization(applyCellList, cellList, edgeList, optimizationAlgorithmConfig);
    if (!isCanceled)
        correctCoordinates();
    //sets the calculated data into cellView's bounds if not canceled 
    if (!isCanceled)
        isCanceled = setNewCoordinates(jgraph);
    //removes the temporary data, stored by the algorithm, from the nodes 
    removeTemporaryLayoutDataFromCells();
    isRunning = false;
}
