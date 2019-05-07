/**
     * A method to be called when an attribute node has been set
     */
void setAttrNode(AttrImpl attr, AttrImpl previous) {
    if (mutationEvents) {
        // MUTATION POST-EVENTS:  
        if (previous == null) {
            dispatchAggregateEvents(attr.ownerNode, attr, null, MutationEvent.ADDITION);
        } else {
            dispatchAggregateEvents(attr.ownerNode, attr, previous.getNodeValue(), MutationEvent.MODIFICATION);
        }
    }
}
