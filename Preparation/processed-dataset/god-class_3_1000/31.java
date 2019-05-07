protected void showCellsForChange(GraphModelEvent.GraphModelChange change) {
    Object[] inserted = change.getInserted();
    if (inserted != null && showsInsertedConnections) {
        for (int i = 0; i < inserted.length; i++) {
            if (!isVisible(inserted[i])) {
                Object source = graphModel.getSource(inserted[i]);
                Object target = graphModel.getTarget(inserted[i]);
                if ((source != null || target != null) && (isVisible(source) && isVisible(target)))
                    setVisibleImpl(new Object[] { inserted[i] }, true);
            }
        }
    }
    if (change.getConnectionSet() != null) {
        Set changedSet = change.getConnectionSet().getChangedEdges();
        if (changedSet != null && showsChangedConnections) {
            Object[] changed = changedSet.toArray();
            for (int i = 0; i < changed.length; i++) {
                if (!isVisible(changed[i])) {
                    Object source = graphModel.getSource(changed[i]);
                    Object target = graphModel.getTarget(changed[i]);
                    if ((source != null || target != null) && (isVisible(source) && isVisible(target)) && !isVisible(changed[i]))
                        setVisibleImpl(new Object[] { changed[i] }, true);
                }
            }
        }
    }
}
