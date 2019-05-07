/**
     * A method to be called when an attribute value has been modified
     */
void modifiedAttrValue(AttrImpl attr, String oldvalue) {
    if (mutationEvents) {
        // MUTATION POST-EVENTS:  
        dispatchAggregateEvents(attr, attr, oldvalue, MutationEvent.MODIFICATION);
    }
}
