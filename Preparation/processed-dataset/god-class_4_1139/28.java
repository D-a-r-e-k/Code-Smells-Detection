// removeEventListener(NodeImpl,String,EventListener,boolean) :void  
protected void copyEventListeners(NodeImpl src, NodeImpl tgt) {
    Vector nodeListeners = getEventListeners(src);
    if (nodeListeners == null) {
        return;
    }
    setEventListeners(tgt, (Vector) nodeListeners.clone());
}
