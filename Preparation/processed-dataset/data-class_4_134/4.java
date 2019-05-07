/******************************************************************************/
/**
     * Runs the Algorithm
     */
public void run(JGraph graph, Object[] dynamic_cells, Object[] static_cells) {
    isRunning = true;
    setAllowedToRun(true);
    setProgress(1);
    //        System.out.println("now running Simulated Annealing"); 
    /*----------------AQUIRATION OF RUNTIME CONSTANTS----------------*/
    jgraph = graph;
    //presetConfig = gpConfiguration; 
    cellList = new ArrayList();
    edgeList = new ArrayList();
    applyCellList = new ArrayList();
    getNodes(jgraph, dynamic_cells);
    if (applyCellList.size() == 0)
        return;
    if (isLayoutUpdateEnabled)
        jgraph.getModel().addGraphModelListener(this);
    /*------------------------AQUIRATION DONE------------------------*/
    /*------------------------ALGORITHM START------------------------*/
    init(true);
    run();
    /*-------------------------ALGORITHM END-------------------------*/
    if (isAllowedToRun()) {
        //if this algorithm isn't a optimization add-on of another algorithm 
        moveGraphToNW();
        //moves the graph to the upper left corner 
        applyChanges();
        // making temporary positions to real positions 
        removeTemporaryData();
    }
    isRunning = false;
}
