/**
	 * If it is a Vertex and it has no source, it must be a root vertex.
	 *
	 * !!!todo I will need to enforce that there is always at least one vertex,
	 * and that is the acq portal.
	 *
	 * @return all the tree root vertices
	 */
protected ArrayList getRootVertices(Object[] selectedCells) {
    HashSet potentialRoot = new HashSet();
    HashSet notARoot = new HashSet();
    CellView viewTargetPort;
    CellView viewTarget;
    /*
		 * Loop through all the vertex and edge cells
		 */
    for (int i = 0; i < selectedCells.length; i++) {
        Object view = jgraph.getGraphLayoutCache().getMapping(selectedCells[i], false);
        /*
			 * If the vertex is not in the notARoot bucket, it is a potential
			 * root.
			 */
        if (view instanceof VertexView) {
            if (!(notARoot.contains(view))) {
                potentialRoot.add(view);
            }
        } else if (view instanceof EdgeView) {
            viewTargetPort = ((EdgeView) view).getTarget();
            viewTarget = viewTargetPort.getParentView();
            potentialRoot.remove(viewTarget);
            notARoot.add(viewTarget);
        } else if (view instanceof PortView) {
        } else {
            throw new RuntimeException("Cell is other than Vertex or Edge.");
        }
    }
    /*
		 * When the loop ends, only tree roots are left
		 */
    return new ArrayList(potentialRoot);
}
