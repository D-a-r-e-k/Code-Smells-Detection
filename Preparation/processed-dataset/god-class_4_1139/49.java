/**
     * A method to be called when character data is about to be replaced in the tree.
     */
void replacingData(NodeImpl node) {
    if (mutationEvents) {
        saveEnclosingAttr(node);
    }
}
