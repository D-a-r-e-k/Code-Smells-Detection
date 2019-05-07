/**
     * Creates or Updates a cache group using the persistence listener.
     * @param groupName The name of the group to update
     * @param group The entries for the group
     */
protected void persistStoreGroup(String groupName, Set group) {
    if (log.isDebugEnabled()) {
        log.debug("persistStoreGroup called (groupName=" + groupName + ")");
    }
    if (persistenceListener != null) {
        try {
            if ((group == null) || group.isEmpty()) {
                persistenceListener.removeGroup(groupName);
            } else {
                persistenceListener.storeGroup(groupName, group);
            }
        } catch (CachePersistenceException e) {
            log.error("[oscache] Exception persisting group " + groupName, e);
        }
    }
}
