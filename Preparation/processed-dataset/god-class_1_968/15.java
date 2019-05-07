/**
	 * This method assumes the graph describes a tree structure. It does not
	 * test to see if the graph is a network.
	 *
	 * Use extreme caution if applyToAll parameter is set to false and
	 * only the selected cells are to be used. In the set of selected cells,
	 * any vertex with no source edge in the set will be considered to be a tree
	 * root. This will work fine as long as all tree vertices in the set that
	 * are a child of a tree vertex in the set are connected by a path of edges
	 * to that parent tree vertex (I think).
	 *
     * @param graph JGraph instance
     * @param dynamic_cells List of all nodes the layout should move
     * @param static_cells List of node the layout should not move but allow for
	 */
public void run(JGraph graph, Object[] dynamic_cells, Object[] static_cells) {
    this.jgraph = graph;
    List roots = getRootVertices(new Object[] { dynamic_cells[0] });
    buildLayoutHelperTree(roots);
    layoutTrees(roots);
    display(roots);
}
