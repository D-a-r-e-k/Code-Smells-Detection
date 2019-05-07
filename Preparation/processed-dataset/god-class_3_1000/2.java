/**
	 * Removes a listener previously added with <B>addGraphLayoutCacheListener()
	 * </B>.
	 * 
	 * @see #addGraphLayoutCacheListener
	 * @param l
	 *            the listener to remove
	 */
public void removeGraphLayoutCacheListener(GraphLayoutCacheListener l) {
    listenerList.remove(GraphLayoutCacheListener.class, l);
}
