/**
     * Retrieve an object from the persistence listener.
     * @param key The key of the object to retrieve
     */
protected Object persistRetrieve(Object key) {
    if (log.isDebugEnabled()) {
        log.debug("persistRetrieve called (key=" + key + ")");
    }
    Object entry = null;
    if (persistenceListener != null) {
        try {
            entry = persistenceListener.retrieve((String) key);
        } catch (CachePersistenceException e) {
        }
    }
    return entry;
}
