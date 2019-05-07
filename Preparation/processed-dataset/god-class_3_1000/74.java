/**
	 * Applies the <code>propertyMap</code> and the connection changes to the
	 * model. The initial <code>edits</code> that triggered the call are
	 * considered to be part of this transaction. Notifies the model- and undo
	 * listeners of the change. Note: The passed in attributes may contain
	 * PortViews.
	 */
public void edit(Map attributes, ConnectionSet cs, ParentMap pm, UndoableEdit[] e) {
    if (attributes != null || cs != null || pm != null || e != null) {
        Object[] visible = null;
        if (isPartial() && showsInvisibleEditedCells) {
            Set tmp = new HashSet();
            if (attributes != null)
                tmp.addAll(attributes.keySet());
            if (cs != null)
                tmp.addAll(cs.getChangedEdges());
            if (pm != null)
                tmp.addAll(pm.getChangedNodes());
            tmp.removeAll(visibleSet);
            if (!tmp.isEmpty())
                visible = tmp.toArray();
        }
        GraphLayoutCacheEdit edit = createLocalEdit(null, attributes, visible, null);
        if (edit != null)
            e = augment(e, edit);
        // Pass to model 
        graphModel.edit(attributes, cs, pm, e);
    }
}
