/**
	 * Hook for subclassers to augment the context for a graphChange. This means
	 * you can add additional cells that should be refreshed on a special change
	 * event. eg. parallel edges when one is removed or added.
	 */
protected Object[] getContext(GraphModelEvent.GraphModelChange change) {
    return change.getContext();
}
