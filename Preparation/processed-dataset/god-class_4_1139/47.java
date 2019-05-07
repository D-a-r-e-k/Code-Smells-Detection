/**
     * A method to be called when a node has been removed from the tree.
     */
void removedNode(NodeImpl node, boolean replace) {
    if (mutationEvents) {
        // MUTATION POST-EVENTS:  
        // Subroutine: Transmit DOMAttrModified and DOMSubtreeModified,  
        // if required. (Common to most kinds of mutation)  
        if (!replace) {
            dispatchAggregateEvents(node, savedEnclosingAttr);
        }
    }
}
