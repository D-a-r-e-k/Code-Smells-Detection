// dispatchEventToSubtree(NodeImpl,Node,Event) :void  
/**
     * Dispatches event to the target node's descendents recursively
     * 
     * @param n node to dispatch to
     * @param e event to be sent to that node and its subtree
     */
protected void dispatchingEventToSubtree(Node n, Event e) {
    if (n == null)
        return;
    // ***** Recursive implementation. This is excessively expensive,  
    // and should be replaced in conjunction with optimization  
    // mentioned above.  
    ((NodeImpl) n).dispatchEvent(e);
    if (n.getNodeType() == Node.ELEMENT_NODE) {
        NamedNodeMap a = n.getAttributes();
        for (int i = a.getLength() - 1; i >= 0; --i) dispatchingEventToSubtree(a.item(i), e);
    }
    dispatchingEventToSubtree(n.getFirstChild(), e);
    dispatchingEventToSubtree(n.getNextSibling(), e);
}
