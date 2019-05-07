/*
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
protected void fireGraphLayoutCacheChanged(Object source, GraphLayoutCacheEvent.GraphLayoutCacheChange edit) {
    // Guaranteed to return a non-null array 
    Object[] listeners = listenerList.getListenerList();
    GraphLayoutCacheEvent e = null;
    // Process the listeners last to first, notifying 
    // those that are interested in this event 
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
        if (listeners[i] == GraphLayoutCacheListener.class) {
            // Lazily create the event: 
            if (e == null)
                e = new GraphLayoutCacheEvent(source, edit);
            ((GraphLayoutCacheListener) listeners[i + 1]).graphLayoutCacheChanged(e);
        }
    }
}
