/**
     * A method to be called when a node is about to be inserted in the tree.
     */
void insertingNode(NodeImpl node, boolean replace) {
    if (mutationEvents) {
        if (!replace) {
            saveEnclosingAttr(node);
        }
    }
}
