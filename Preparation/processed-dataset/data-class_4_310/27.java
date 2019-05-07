// addEventListener(NodeImpl,String,EventListener,boolean) :void  
/**
     * Introduced in DOM Level 2. <p> Deregister an event listener previously
     * registered with this Node.  A listener must be independently removed
     * from the Capturing and Bubbling roles. Redundant removals (of listeners
     * not currently registered for this role) are ignored.
     * @param node node to remove listener from
     * @param type Event name (NOT event group!) to listen for.
     * @param listener Who gets called when event is dispatched
     * @param useCapture True iff listener is registered on
     *  capturing phase rather than at-target or bubbling
     */
protected void removeEventListener(NodeImpl node, String type, EventListener listener, boolean useCapture) {
    // If this couldn't be a valid listener registration, ignore request  
    if (type == null || type.length() == 0 || listener == null)
        return;
    Vector nodeListeners = getEventListeners(node);
    if (nodeListeners == null)
        return;
    // Note that addListener has previously ensured that   
    // each listener may be registered only once per type per phase.  
    // count-down is OK for deletions!  
    for (int i = nodeListeners.size() - 1; i >= 0; --i) {
        LEntry le = (LEntry) nodeListeners.elementAt(i);
        if (le.useCapture == useCapture && le.listener == listener && le.type.equals(type)) {
            nodeListeners.removeElementAt(i);
            // Storage management: Discard empty listener lists  
            if (nodeListeners.size() == 0)
                setEventListeners(node, null);
            // Remove active listener  
            LCount lc = LCount.lookup(type);
            if (useCapture) {
                --lc.captures;
                --lc.total;
            } else {
                --lc.bubbles;
                --lc.total;
            }
            break;
        }
    }
}
