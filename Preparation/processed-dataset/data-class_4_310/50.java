/**
     * A method to be called when a node has been replaced in the tree.
     */
void replacedNode(NodeImpl node) {
    if (mutationEvents) {
        dispatchAggregateEvents(node, savedEnclosingAttr);
    }
}
