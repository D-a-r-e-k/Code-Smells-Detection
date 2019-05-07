/**
	 * Hook for subclassers to return the first or last visible port to replace
	 * the current source or target port of the edge. This is called when groups
	 * are collapsed for the edges that cross the group, ie. go from a child
	 * cell to a cell which is outside the group. This implementation returns
	 * the first port of the parent group if source is true, otherwise it
	 * returns the last port of the parent group.
	 */
protected Object getParentPort(Object edge, boolean source) {
    // Contains the parent of the parent vertex, eg. the group 
    Object parent = getModel().getParent((source) ? DefaultGraphModel.getSourceVertex(getModel(), edge) : DefaultGraphModel.getTargetVertex(getModel(), edge));
    // Finds a port in the group 
    int c = getModel().getChildCount(parent);
    for (int i = (source) ? c - 1 : 0; i < getModel().getChildCount(parent) && i >= 0; i += (source) ? -1 : +1) {
        Object child = getModel().getChild(parent, i);
        if (getModel().isPort(child)) {
            return child;
        }
    }
    return null;
}
