/**
     * Updates the groups to reflect the differences between the old and new
     * cache entries. Either of the old or new values can be <code>null</code>
     * or contain a <code>null</code> group list, in which case the entry's
     * groups will all be added or removed respectively.
     *
     * @param oldValue The old CacheEntry that is being replaced.
     * @param newValue The new CacheEntry that is being inserted.
     */
private void updateGroups(Object oldValue, Object newValue, boolean persist) {
    // If we have/had a CacheEntry, update the group lookups  
    boolean oldIsCE = oldValue instanceof CacheEntry;
    boolean newIsCE = newValue instanceof CacheEntry;
    if (newIsCE && oldIsCE) {
        updateGroups((CacheEntry) oldValue, (CacheEntry) newValue, persist);
    } else if (newIsCE) {
        updateGroups(null, (CacheEntry) newValue, persist);
    } else if (oldIsCE) {
        updateGroups((CacheEntry) oldValue, null, persist);
    }
}
