// LEntry  
/**
     * Introduced in DOM Level 2. <p> Register an event listener with this
     * Node. A listener may be independently registered as both Capturing and
     * Bubbling, but may only be registered once per role; redundant
     * registrations are ignored.
     * @param node node to add listener to
     * @param type Event name (NOT event group!) to listen for.
     * @param listener Who gets called when event is dispatched
     * @param useCapture True iff listener is registered on
     *  capturing phase rather than at-target or bubbling
     */
protected void addEventListener(NodeImpl node, String type, EventListener listener, boolean useCapture) {
    // We can't dispatch to blank type-name, and of course we need  
    // a listener to dispatch to  
    if (type == null || type.length() == 0 || listener == null)
        return;
    // Each listener may be registered only once per type per phase.  
    // Simplest way to code that is to zap the previous entry, if any.  
    removeEventListener(node, type, listener, useCapture);
    Vector nodeListeners = getEventListeners(node);
    if (nodeListeners == null) {
        nodeListeners = new Vector();
        setEventListeners(node, nodeListeners);
    }
    nodeListeners.addElement(new LEntry(type, listener, useCapture));
    // Record active listener  
    LCount lc = LCount.lookup(type);
    if (useCapture) {
        ++lc.captures;
        ++lc.total;
    } else {
        ++lc.bubbles;
        ++lc.total;
    }
}
