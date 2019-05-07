/**
     * Remove an object from the persistence.
     * @param key The key of the object to remove
     */
protected void persistRemove(Object key) {
    if (log.isDebugEnabled()) {
        log.debug("PersistRemove called (key=" + key + ")");
    }
    if (persistenceListener != null) {
        try {
            persistenceListener.remove((String) key);
        } catch (CachePersistenceException e) {
            log.error("[oscache] Exception removing cache entry with key '" + key + "' from persistence", e);
        }
    }
}
