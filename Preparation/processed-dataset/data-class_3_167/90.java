/**
	 * Returns a collection of cells that are connected to the specified cell by
	 * edges. Any cells specified in the exclude set will be ignored.
	 * 
	 * @param cell
	 *            The cell from which the neighbours will be determined
	 * @param exclude
	 *            The set of cells to ignore when searching
	 * @param directed
	 *            whether or not direction of edges should be taken into account
	 * @param visibleCells
	 *            whether or not to only consider visible cells
	 * @return Returns the list of neighbours for <code>cell</code>
	 */
public List getNeighbours(Object cell, Set exclude, boolean directed, boolean visibleCells) {
    // Traverse Graph 
    GraphModel model = getModel();
    Object[] fanout = (directed) ? DefaultGraphModel.getOutgoingEdges(model, cell) : DefaultGraphModel.getEdges(model, new Object[] { cell }).toArray();
    List neighbours = new ArrayList(fanout.length);
    Set localExclude = new HashSet(fanout.length + 8, (float) 0.75);
    for (int i = 0; i < fanout.length; i++) {
        // if only visible cells are being processed, check that this 
        // edge is visible before looking for neighbours with it 
        if (!visibleCells || isVisible(fanout[i])) {
            Object neighbour = DefaultGraphModel.getOpposite(model, fanout[i], cell);
            if (neighbour != null && (exclude == null || !exclude.contains(neighbour)) && !localExclude.contains(neighbour) && (!visibleCells || isVisible(neighbour))) {
                localExclude.add(neighbour);
                neighbours.add(neighbour);
            }
        }
    }
    return neighbours;
}
