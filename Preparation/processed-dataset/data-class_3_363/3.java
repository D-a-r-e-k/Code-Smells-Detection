/**
     * Inform all registered listeners that the transaction was committed.
     */
private void txcommitted() {
    for (int i = 0; i < _synchronizeList.size(); i++) {
        TxSynchronizable sync = _synchronizeList.get(i);
        try {
            sync.committed(this);
        } catch (Exception ex) {
            String cls = sync.getClass().getName();
            LOG.warn("Exception at " + cls + ".committed()", ex);
        }
    }
}
