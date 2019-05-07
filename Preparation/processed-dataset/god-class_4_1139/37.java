private void mutationEventsModifiedCharacterData(NodeImpl node, String oldvalue, String value, boolean replace) {
    if (!replace) {
        // MUTATION POST-EVENTS:  
        LCount lc = LCount.lookup(MutationEventImpl.DOM_CHARACTER_DATA_MODIFIED);
        if (lc.total > 0) {
            MutationEvent me = new MutationEventImpl();
            me.initMutationEvent(MutationEventImpl.DOM_CHARACTER_DATA_MODIFIED, true, false, null, oldvalue, value, null, (short) 0);
            dispatchEvent(node, me);
        }
        // Subroutine: Transmit DOMAttrModified and DOMSubtreeModified,  
        // if required. (Common to most kinds of mutation)  
        dispatchAggregateEvents(node, savedEnclosingAttr);
    }
}
