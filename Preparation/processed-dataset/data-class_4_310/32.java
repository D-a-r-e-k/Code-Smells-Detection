/**
     * NON-DOM INTERNAL: Convenience wrapper for calling
     * dispatchAggregateEvents when the context was established
     * by <code>savedEnclosingAttr</code>.
     * @param node node to dispatch to
     * @param ea description of Attr affected by current operation
     */
protected void dispatchAggregateEvents(NodeImpl node, EnclosingAttr ea) {
    if (ea != null)
        dispatchAggregateEvents(node, ea.node, ea.oldvalue, MutationEvent.MODIFICATION);
    else
        dispatchAggregateEvents(node, null, null, (short) 0);
}
