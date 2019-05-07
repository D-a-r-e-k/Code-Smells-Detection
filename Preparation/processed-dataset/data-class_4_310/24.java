/**
     * Store event listener registered on a given node
     * This is another place where we could use weak references! Indeed, the
     * node here won't be GC'ed as long as some listener is registered on it,
     * since the eventsListeners table will have a reference to the node.
     */
protected void setEventListeners(NodeImpl n, Vector listeners) {
    if (eventListeners == null) {
        eventListeners = new Hashtable();
    }
    if (listeners == null) {
        eventListeners.remove(n);
        if (eventListeners.isEmpty()) {
            // stop firing events when there isn't any listener  
            mutationEvents = false;
        }
    } else {
        eventListeners.put(n, listeners);
        // turn mutation events on  
        mutationEvents = true;
    }
}
