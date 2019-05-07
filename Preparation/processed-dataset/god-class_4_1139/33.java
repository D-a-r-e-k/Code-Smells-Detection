// dispatchAggregateEvents(NodeImpl,EnclosingAttr) :void  
/**
     * NON-DOM INTERNAL: Generate the "aggregated" post-mutation events
     * DOMAttrModified and DOMSubtreeModified.
     * Both of these should be issued only once for each user-requested
     * mutation operation, even if that involves multiple changes to
     * the DOM.
     * For example, if a DOM operation makes multiple changes to a single
     * Attr before returning, it would be nice to generate only one 
     * DOMAttrModified, and multiple changes over larger scope but within
     * a recognizable single subtree might want to generate only one 
     * DOMSubtreeModified, sent to their lowest common ancestor. 
     * <p>
     * To manage this, use the "internal" versions of insert and remove
     * with MUTATION_LOCAL, then make an explicit call to this routine
     * at the higher level. Some examples now exist in our code.
     *
     * @param node The node to dispatch to
     * @param enclosingAttr The Attr node (if any) whose value has been changed
     * as a result of the DOM operation. Null if none such.
     * @param oldvalue The String value previously held by the
     * enclosingAttr. Ignored if none such.
     * @param change Type of modification to the attr. See
     * MutationEvent.attrChange
     */
protected void dispatchAggregateEvents(NodeImpl node, AttrImpl enclosingAttr, String oldvalue, short change) {
    // We have to send DOMAttrModified.  
    NodeImpl owner = null;
    if (enclosingAttr != null) {
        LCount lc = LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
        owner = (NodeImpl) enclosingAttr.getOwnerElement();
        if (lc.total > 0) {
            if (owner != null) {
                MutationEventImpl me = new MutationEventImpl();
                me.initMutationEvent(MutationEventImpl.DOM_ATTR_MODIFIED, true, false, enclosingAttr, oldvalue, enclosingAttr.getNodeValue(), enclosingAttr.getNodeName(), change);
                owner.dispatchEvent(me);
            }
        }
    }
    // DOMSubtreeModified gets sent to the lowest common root of a  
    // set of changes.   
    // "This event is dispatched after all other events caused by the  
    // mutation have been fired."  
    LCount lc = LCount.lookup(MutationEventImpl.DOM_SUBTREE_MODIFIED);
    if (lc.total > 0) {
        MutationEvent me = new MutationEventImpl();
        me.initMutationEvent(MutationEventImpl.DOM_SUBTREE_MODIFIED, true, false, null, null, null, null, (short) 0);
        // If we're within an Attr, DStM gets sent to the Attr  
        // and to its owningElement. Otherwise we dispatch it  
        // locally.  
        if (enclosingAttr != null) {
            dispatchEvent(enclosingAttr, me);
            if (owner != null)
                dispatchEvent(owner, me);
        } else
            dispatchEvent(node, me);
    }
}
