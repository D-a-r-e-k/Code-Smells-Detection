/**
     * A method to be called when a node has been inserted in the tree.
     */
void insertedNode(NodeImpl node, NodeImpl newInternal, boolean replace) {
    if (mutationEvents) {
        mutationEventsInsertedNode(node, newInternal, replace);
    }
    // notify the range of insertions  
    if (ranges != null) {
        notifyRangesInsertedNode(newInternal);
    }
}
