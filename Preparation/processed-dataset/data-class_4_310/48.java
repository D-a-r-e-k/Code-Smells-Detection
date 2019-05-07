/**
     * A method to be called when a node is about to be replaced in the tree.
     */
void replacingNode(NodeImpl node) {
    if (mutationEvents) {
        saveEnclosingAttr(node);
    }
}
