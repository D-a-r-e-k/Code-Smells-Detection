/**
     * Retrieves a cache group using the persistence listener.
     * @param groupName The name of the group to retrieve
     */
protected Set persistRetrieveGroup(String groupName) {
    if (log.isDebugEnabled()) {
        log.debug("persistRetrieveGroup called (groupName=" + groupName + ")");
    }
    if (persistenceListener != null) {
        try {
            return persistenceListener.retrieveGroup(groupName);
        } catch (CachePersistenceException e) {
            log.error("[oscache] Exception retrieving group " + groupName, e);
        }
    }
    return null;
}
