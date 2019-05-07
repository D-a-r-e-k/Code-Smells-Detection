/**
     * Removes a cache group using the persistence listener.
     * @param groupName The name of the group to remove
     */
protected void persistRemoveGroup(String groupName) {
    if (log.isDebugEnabled()) {
        log.debug("persistRemoveGroup called (groupName=" + groupName + ")");
    }
    if (persistenceListener != null) {
        try {
            persistenceListener.removeGroup(groupName);
        } catch (CachePersistenceException e) {
            log.error("[oscache] Exception removing group " + groupName, e);
        }
    }
}
