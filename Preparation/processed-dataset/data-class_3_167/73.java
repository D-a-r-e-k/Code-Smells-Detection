/**
	 * Hook for subclassers to return the port to be used for edges that have
	 * been connected to the group. This is called from expand. This returns the
	 * first port of the first or last vertex depending on <code>source</code>.
	 */
protected Object getChildPort(Object edge, boolean source) {
    GraphModel model = getModel();
    // Contains the parent of the port, eg. the group 
    Object parent = (source) ? DefaultGraphModel.getSourceVertex(model, edge) : DefaultGraphModel.getTargetVertex(model, edge);
    // Finds a vertex in the group 
    int c = model.getChildCount(parent);
    for (int i = (source) ? c - 1 : 0; i < c && i >= 0; i += (source) ? -1 : +1) {
        Object child = model.getChild(parent, i);
        if (!model.isEdge(child) && !model.isPort(child)) {
            // Finds a port in the vertex 
            for (int j = 0; j < model.getChildCount(child); j++) {
                Object port = model.getChild(child, j);
                if (model.isPort(port)) {
                    return port;
                }
            }
        }
    }
    return null;
}
