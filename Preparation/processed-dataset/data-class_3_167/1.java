// 
// GraphLayoutCacheListeners 
// 
/**
	 * Adds a listener for the GraphLayoutCacheEvent posted after the graph
	 * layout cache changes.
	 * 
	 * @see #removeGraphLayoutCacheListener
	 * @param l
	 *            the listener to add
	 */
public void addGraphLayoutCacheListener(GraphLayoutCacheListener l) {
    listenerList.add(GraphLayoutCacheListener.class, l);
}
