private void mutationEventsInsertedNode(NodeImpl node, NodeImpl newInternal, boolean replace) {
    // MUTATION POST-EVENTS:  
    // "Local" events (non-aggregated)  
    // New child is told it was inserted, and where  
    LCount lc = LCount.lookup(MutationEventImpl.DOM_NODE_INSERTED);
    if (lc.total > 0) {
        MutationEventImpl me = new MutationEventImpl();
        me.initMutationEvent(MutationEventImpl.DOM_NODE_INSERTED, true, false, node, null, null, null, (short) 0);
        dispatchEvent(newInternal, me);
    }
    // If within the Document, tell the subtree it's been added  
    // to the Doc.  
    lc = LCount.lookup(MutationEventImpl.DOM_NODE_INSERTED_INTO_DOCUMENT);
    if (lc.total > 0) {
        NodeImpl eventAncestor = node;
        if (savedEnclosingAttr != null)
            eventAncestor = (NodeImpl) savedEnclosingAttr.node.getOwnerElement();
        if (eventAncestor != null) {
            // Might have been orphan Attr  
            NodeImpl p = eventAncestor;
            while (p != null) {
                eventAncestor = p;
                // Last non-null ancestor  
                // In this context, ancestry includes  
                // walking back from Attr to Element  
                if (p.getNodeType() == ATTRIBUTE_NODE) {
                    p = (NodeImpl) ((AttrImpl) p).getOwnerElement();
                } else {
                    p = p.parentNode();
                }
            }
            if (eventAncestor.getNodeType() == Node.DOCUMENT_NODE) {
                MutationEventImpl me = new MutationEventImpl();
                me.initMutationEvent(MutationEventImpl.DOM_NODE_INSERTED_INTO_DOCUMENT, false, false, null, null, null, null, (short) 0);
                dispatchEventToSubtree(newInternal, me);
            }
        }
    }
    if (!replace) {
        // Subroutine: Transmit DOMAttrModified and DOMSubtreeModified  
        // (Common to most kinds of mutation)  
        dispatchAggregateEvents(node, savedEnclosingAttr);
    }
}
