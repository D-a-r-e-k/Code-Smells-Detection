// dispatchEvent(NodeImpl,Event) :boolean  
/**
     * NON-DOM INTERNAL: DOMNodeInsertedIntoDocument and ...RemovedFrom...
     * are dispatched to an entire subtree. This is the distribution code
     * therefor. They DO NOT bubble, thanks be, but may be captured.
     * <p>
     * Similar to code in dispatchingEventToSubtree however this method
     * is only used on the target node and does not start a dispatching chain
     * on the sibling of the target node as this is not part of the subtree 
     * ***** At the moment I'm being sloppy and using the normal
     * capture dispatcher on every node. This could be optimized hugely
     * by writing a capture engine that tracks our position in the tree to
     * update the capture chain without repeated chases up to root.
     * @param n target node (that was directly inserted or removed)
     * @param e event to be sent to that node and its subtree
     */
protected void dispatchEventToSubtree(Node n, Event e) {
    ((NodeImpl) n).dispatchEvent(e);
    if (n.getNodeType() == Node.ELEMENT_NODE) {
        NamedNodeMap a = n.getAttributes();
        for (int i = a.getLength() - 1; i >= 0; --i) dispatchingEventToSubtree(a.item(i), e);
    }
    dispatchingEventToSubtree(n.getFirstChild(), e);
}
