/**
     * Store an object in the cache using the persistence listener.
     * @param key The object key
     * @param obj The object to store
     */
protected void persistStore(Object key, Object obj) {
    if (log.isDebugEnabled()) {
        log.debug("persistStore called (key=" + key + ")");
    }
    if (persistenceListener != null) {
        try {
            persistenceListener.store((String) key, obj);
        } catch (CachePersistenceException e) {
            log.error("[oscache] Exception persisting " + key, e);
        }
    }
}
