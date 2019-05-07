/**
     * Inform all registered listeners that the transaction was rolled back.
     */
private void txrolledback() {
    for (int i = 0; i < _synchronizeList.size(); i++) {
        TxSynchronizable sync = _synchronizeList.get(i);
        try {
            sync.rolledback(this);
        } catch (Exception ex) {
            String cls = sync.getClass().getName();
            LOG.warn("Exception at " + cls + ".rolledback()", ex);
        }
    }
}
