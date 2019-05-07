/**
	 * Returns the outgoing edges for cell. Cell should be a port or a vertex.
	 * 
	 * @param cell
	 *            The cell from which the outgoing edges will be determined
	 * @param exclude
	 *            The set of edges to ignore when searching
	 * @param visibleCells
	 *            whether or not only visible cells should be processed
	 * @param selfLoops
	 *            whether or not to include self loops in the returned list
	 * @return Returns the list of outgoing edges for <code>cell</code>
	 */
public List getOutgoingEdges(Object cell, Set exclude, boolean visibleCells, boolean selfLoops) {
    return getEdges(cell, exclude, visibleCells, selfLoops, false);
}
