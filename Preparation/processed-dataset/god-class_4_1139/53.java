/**
     * A method to be called when an attribute node has been removed
     */
void removedAttrNode(AttrImpl attr, NodeImpl oldOwner, String name) {
    // We can't use the standard dispatchAggregate, since it assumes  
    // that the Attr is still attached to an owner. This code is  
    // similar but dispatches to the previous owner, "element".  
    if (mutationEvents) {
        mutationEventsRemovedAttrNode(attr, oldOwner, name);
    }
}
