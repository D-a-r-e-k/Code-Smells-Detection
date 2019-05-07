/**
     * Get ref to group.
     * CACHE-127 Synchronized copying of the group entry set since
     * the new HashSet(Collection c) constructor uses the iterator.
     * This may slow things down but it is better than a
     * ConcurrentModificationException.  We might have to revisit the
     * code if performance is too adversely impacted.
     **/
protected final synchronized Set getGroupForReading(String groupName) {
    Set group = (Set) getGroupsForReading().get(groupName);
    if (group == null)
        return null;
    return new HashSet(group);
}
