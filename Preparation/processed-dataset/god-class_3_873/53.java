/**
     * Add this cache key to the groups specified groups.
     * We have to treat the
     * memory and disk group mappings seperately so they remain valid for their
     * corresponding memory/disk caches. (eg if mem is limited to 100 entries
     * and disk is unlimited, the group mappings will be different).
     *
     * @param key The cache key that we are ading to the groups.
     * @param newGroups the set of groups we want to add this cache entry to.
     * @param persist A flag to indicate whether the keys should be added to
     * the persistent cache layer.
     * @param memory A flag to indicate whether the key should be added to
     * the memory groups (important for overflow-to-disk)
     */
private void addGroupMappings(String key, Set newGroups, boolean persist, boolean memory) {
    if (newGroups == null) {
        return;
    }
    // Add this CacheEntry to the groups that it is now a member of  
    for (Iterator it = newGroups.iterator(); it.hasNext(); ) {
        String groupName = (String) it.next();
        // Update the in-memory groups  
        if (memoryCaching && memory) {
            if (groups == null) {
                groups = new HashMap();
            }
            Set memoryGroup = (Set) groups.get(groupName);
            if (memoryGroup == null) {
                memoryGroup = new HashSet();
                groups.put(groupName, memoryGroup);
            }
            memoryGroup.add(key);
        }
        // Update the persistent group maps  
        if (persist) {
            Set persistentGroup = persistRetrieveGroup(groupName);
            if (persistentGroup == null) {
                persistentGroup = new HashSet();
            }
            persistentGroup.add(key);
            persistStoreGroup(groupName, persistentGroup);
        }
    }
}
