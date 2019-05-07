/**
	 * Return an array of all GraphLayoutCacheListener that were added to this
	 * model.
	 */
public GraphLayoutCacheListener[] getGraphLayoutCacheListeners() {
    return (GraphLayoutCacheListener[]) listenerList.getListeners(GraphLayoutCacheListener.class);
}
