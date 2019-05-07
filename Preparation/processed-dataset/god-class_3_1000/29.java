protected void hideCellsForChange(GraphModelEvent.GraphModelChange change) {
    // Hide visible edges between invisible vertices 
    // 1. Remove attached edges of removed cells 
    // 2. Remove edges who's source or target has changed to 
    // invisible. 
    Object[] tmp = change.getRemoved();
    Set removed = new HashSet();
    if (tmp != null)
        for (int i = 0; i < tmp.length; i++) removed.add(tmp[i]);
    if (hidesDanglingConnections || hidesExistingConnections) {
        Object[] changed = change.getChanged();
        for (int i = 0; i < changed.length; i++) {
            CellView view = getMapping(changed[i], false);
            if (view instanceof EdgeView) {
                EdgeView edge = (EdgeView) view;
                Object oldSource = (edge.getSource() == null) ? null : edge.getSource().getCell();
                Object oldTarget = (edge.getTarget() == null) ? null : edge.getTarget().getCell();
                Object newSource = graphModel.getSource(changed[i]);
                Object newTarget = graphModel.getTarget(changed[i]);
                boolean hideExisting = (hidesExistingConnections && ((newSource != null && !hasVisibleParent(newSource, null)) || (newTarget != null && !hasVisibleParent(newTarget, null))));
                if ((hidesDanglingConnections && (removed.contains(oldSource) || removed.contains(oldTarget))) || hideExisting) {
                    setVisibleImpl(new Object[] { changed[i] }, false);
                }
            }
        }
    }
}
