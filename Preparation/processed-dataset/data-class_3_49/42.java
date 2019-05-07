/**
     * Removes the entire cache from persistent storage.
     */
protected void persistClear() {
    if (log.isDebugEnabled()) {
        log.debug("persistClear called");
        ;
    }
    if (persistenceListener != null) {
        try {
            persistenceListener.clear();
        } catch (CachePersistenceException e) {
            log.error("[oscache] Exception clearing persistent cache", e);
        }
    }
}
