// dispatchAggregateEvents(NodeImpl, AttrImpl,String) :void  
/**
     * NON-DOM INTERNAL: Pre-mutation context check, in
     * preparation for later generating DOMAttrModified events.
     * Determines whether this node is within an Attr
     * @param node node to get enclosing attribute for
     */
protected void saveEnclosingAttr(NodeImpl node) {
    savedEnclosingAttr = null;
    // MUTATION PREPROCESSING AND PRE-EVENTS:  
    // If we're within the scope of an Attr and DOMAttrModified   
    // was requested, we need to preserve its previous value for  
    // that event.  
    LCount lc = LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
    if (lc.total > 0) {
        NodeImpl eventAncestor = node;
        while (true) {
            if (eventAncestor == null)
                return;
            int type = eventAncestor.getNodeType();
            if (type == Node.ATTRIBUTE_NODE) {
                EnclosingAttr retval = new EnclosingAttr();
                retval.node = (AttrImpl) eventAncestor;
                retval.oldvalue = retval.node.getNodeValue();
                savedEnclosingAttr = retval;
                return;
            } else if (type == Node.ENTITY_REFERENCE_NODE)
                eventAncestor = eventAncestor.parentNode();
            else if (type == Node.TEXT_NODE)
                eventAncestor = eventAncestor.parentNode();
            else
                return;
        }
    }
}
