/******************************************************************************/
/**
     * Runs the Algorithm as a optimization Algorithm of another Algorithm
     * @param applyList List of all Cells, a new Layout should be found for.
     * @param allCellList List of all Cells of the Graph
     * @param allEdgeList List of all Edges of the Graph
     */
public void performOptimization(ArrayList applyList, ArrayList allCellList, ArrayList allEdgeList, Properties config) {
    cellList = allCellList;
    applyCellList = applyList;
    edgeList = allEdgeList;
    presetConfig = config;
    loadConfiguration(CONFIG_KEY_RUN);
    init(false);
    run();
}
