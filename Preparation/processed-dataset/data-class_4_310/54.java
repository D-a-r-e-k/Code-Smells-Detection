private void mutationEventsRemovedAttrNode(AttrImpl attr, NodeImpl oldOwner, String name) {
    // If we have to send DOMAttrModified (determined earlier),  
    // do so.  
    LCount lc = LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
    if (lc.total > 0) {
        MutationEventImpl me = new MutationEventImpl();
        me.initMutationEvent(MutationEventImpl.DOM_ATTR_MODIFIED, true, false, attr, attr.getNodeValue(), null, name, MutationEvent.REMOVAL);
        dispatchEvent(oldOwner, me);
    }
    // We can hand off to process DOMSubtreeModified, though.  
    // Note that only the Element needs to be informed; the  
    // Attr's subtree has not been changed by this operation.  
    dispatchAggregateEvents(oldOwner, null, null, (short) 0);
}
