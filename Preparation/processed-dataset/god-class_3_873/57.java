/**
     * Remove this CacheEntry from the groups it no longer belongs to.
     * We have to treat the memory and disk group mappings separately so they remain
     * valid for their corresponding memory/disk caches. (eg if mem is limited
     * to 100 entries and disk is unlimited, the group mappings will be
     * different).
     *
     * @param key The cache key that we are removing from the groups.
     * @param oldGroups the set of groups we want to remove the cache entry
     * from.
     * @param persist A flag to indicate whether the keys should be removed
     * from the persistent cache layer.
     */
private void removeGroupMappings(String key, Set oldGroups, boolean persist) {
    if (oldGroups == null) {
        return;
    }
    for (Iterator it = oldGroups.iterator(); it.hasNext(); ) {
        String groupName = (String) it.next();
        // Update the in-memory groups  
        if (memoryCaching && (this.groups != null)) {
            Set memoryGroup = (Set) groups.get(groupName);
            if (memoryGroup != null) {
                memoryGroup.remove(key);
                if (memoryGroup.isEmpty()) {
                    groups.remove(groupName);
                }
            }
        }
        // Update the persistent group maps  
        if (persist) {
            Set persistentGroup = persistRetrieveGroup(groupName);
            if (persistentGroup != null) {
                persistentGroup.remove(key);
                if (persistentGroup.isEmpty()) {
                    persistRemoveGroup(groupName);
                } else {
                    persistStoreGroup(groupName, persistentGroup);
                }
            }
        }
    }
}
