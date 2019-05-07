/**
     * Retreive event listener registered on a given node
     */
protected Vector getEventListeners(NodeImpl n) {
    if (eventListeners == null) {
        return null;
    }
    return (Vector) eventListeners.get(n);
}
