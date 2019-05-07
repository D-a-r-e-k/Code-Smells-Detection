private void mutationEventsRemovingNode(NodeImpl node, NodeImpl oldChild, boolean replace) {
    // MUTATION PREPROCESSING AND PRE-EVENTS:  
    // If we're within the scope of an Attr and DOMAttrModified   
    // was requested, we need to preserve its previous value for  
    // that event.  
    if (!replace) {
        saveEnclosingAttr(node);
    }
    // Child is told that it is about to be removed  
    LCount lc = LCount.lookup(MutationEventImpl.DOM_NODE_REMOVED);
    if (lc.total > 0) {
        MutationEventImpl me = new MutationEventImpl();
        me.initMutationEvent(MutationEventImpl.DOM_NODE_REMOVED, true, false, node, null, null, null, (short) 0);
        dispatchEvent(oldChild, me);
    }
    // If within Document, child's subtree is informed that it's  
    // losing that status  
    lc = LCount.lookup(MutationEventImpl.DOM_NODE_REMOVED_FROM_DOCUMENT);
    if (lc.total > 0) {
        NodeImpl eventAncestor = this;
        if (savedEnclosingAttr != null)
            eventAncestor = (NodeImpl) savedEnclosingAttr.node.getOwnerElement();
        if (eventAncestor != null) {
            // Might have been orphan Attr  
            for (NodeImpl p = eventAncestor.parentNode(); p != null; p = p.parentNode()) {
                eventAncestor = p;
            }
            if (eventAncestor.getNodeType() == Node.DOCUMENT_NODE) {
                MutationEventImpl me = new MutationEventImpl();
                me.initMutationEvent(MutationEventImpl.DOM_NODE_REMOVED_FROM_DOCUMENT, false, false, null, null, null, null, (short) 0);
                dispatchEventToSubtree(oldChild, me);
            }
        }
    }
}
