/**
     * Updates the groups to reflect the differences between the old and new cache entries.
     * Either of the old or new values can be <code>null</code>
     * or contain a <code>null</code> group list, in which case the entry's
     * groups will all be added or removed respectively.
     *
     * @param oldValue The old CacheEntry that is being replaced.
     * @param newValue The new CacheEntry that is being inserted.
     */
private void updateGroups(CacheEntry oldValue, CacheEntry newValue, boolean persist) {
    Set oldGroups = null;
    Set newGroups = null;
    if (oldValue != null) {
        oldGroups = oldValue.getGroups();
    }
    if (newValue != null) {
        newGroups = newValue.getGroups();
    }
    // Get the names of the groups to remove  
    if (oldGroups != null) {
        Set removeFromGroups = new HashSet();
        for (Iterator it = oldGroups.iterator(); it.hasNext(); ) {
            String groupName = (String) it.next();
            if ((newGroups == null) || !newGroups.contains(groupName)) {
                // We need to remove this group  
                removeFromGroups.add(groupName);
            }
        }
        removeGroupMappings(oldValue.getKey(), removeFromGroups, persist);
    }
    // Get the names of the groups to add  
    if (newGroups != null) {
        Set addToGroups = new HashSet();
        for (Iterator it = newGroups.iterator(); it.hasNext(); ) {
            String groupName = (String) it.next();
            if ((oldGroups == null) || !oldGroups.contains(groupName)) {
                // We need to add this group  
                addToGroups.add(groupName);
            }
        }
        addGroupMappings(newValue.getKey(), addToGroups, persist, true);
    }
}
