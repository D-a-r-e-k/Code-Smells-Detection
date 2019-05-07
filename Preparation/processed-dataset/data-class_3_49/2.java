/**
     * Returns a set of the cache keys that reside in a particular group.
     *
     * @param   groupName The name of the group to retrieve.
     * @return  a set containing all of the keys of cache entries that belong
     * to this group, or <code>null</code> if the group was not found.
     * @exception  NullPointerException if the groupName is <code>null</code>.
     */
public Set getGroup(String groupName) {
    if (log.isDebugEnabled()) {
        log.debug("getGroup called (group=" + groupName + ")");
    }
    Set groupEntries = null;
    if (memoryCaching && (groups != null)) {
        groupEntries = (Set) getGroupForReading(groupName);
    }
    if (groupEntries == null) {
        // Not in the map, try the persistence layer  
        groupEntries = persistRetrieveGroup(groupName);
    }
    return groupEntries;
}
