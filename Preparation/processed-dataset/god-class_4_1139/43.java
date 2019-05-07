/**
     * A method to be called when a node is about to be removed from the tree.
     */
void removingNode(NodeImpl node, NodeImpl oldChild, boolean replace) {
    // notify iterators  
    if (iterators != null) {
        notifyIteratorsRemovingNode(oldChild);
    }
    // notify ranges  
    if (ranges != null) {
        notifyRangesRemovingNode(oldChild);
    }
    // mutation events  
    if (mutationEvents) {
        mutationEventsRemovingNode(node, oldChild, replace);
    }
}
