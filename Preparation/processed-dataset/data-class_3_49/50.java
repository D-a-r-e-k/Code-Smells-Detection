/**
     * Continuation of remove(), called only when synch lock is
     * held and interference has been detected.
     **/
/** OpenSymphony BEGIN */
/* Previous code
    protected Object sremove(Object key, int hash) { */
protected Object sremove(Object key, int hash, boolean invokeAlgorithm) {
    /** OpenSymphony END */
    Entry[] tab = table;
    int index = hash & (tab.length - 1);
    Entry first = tab[index];
    Entry e = first;
    for (; ; ) {
        if (e == null) {
            return null;
        } else if ((key == e.key) || ((e.hash == hash) && key.equals(e.key))) {
            Object oldValue = e.value;
            if (persistenceListener != null && (oldValue == NULL)) {
                oldValue = persistRetrieve(key);
            }
            e.value = null;
            count--;
            /** OpenSymphony BEGIN */
            if (!unlimitedDiskCache && !overflowPersistence) {
                persistRemove(e.key);
                // If we have a CacheEntry, update the groups  
                if (oldValue instanceof CacheEntry) {
                    CacheEntry oldEntry = (CacheEntry) oldValue;
                    removeGroupMappings(oldEntry.getKey(), oldEntry.getGroups(), true);
                }
            } else {
                // only remove from memory groups  
                if (oldValue instanceof CacheEntry) {
                    CacheEntry oldEntry = (CacheEntry) oldValue;
                    removeGroupMappings(oldEntry.getKey(), oldEntry.getGroups(), false);
                }
            }
            if (overflowPersistence && ((size() + 1) >= maxEntries)) {
                persistStore(key, oldValue);
                // add key to persistent groups but NOT to the memory groups  
                if (oldValue instanceof CacheEntry) {
                    CacheEntry oldEntry = (CacheEntry) oldValue;
                    addGroupMappings(oldEntry.getKey(), oldEntry.getGroups(), true, false);
                }
            }
            if (invokeAlgorithm) {
                itemRemoved(key);
            }
            /** OpenSymphony END */
            Entry head = e.next;
            for (Entry p = first; p != e; p = p.next) {
                head = new Entry(p.hash, p.key, p.value, head);
            }
            tab[index] = head;
            recordModification(head);
            return oldValue;
        } else {
            e = e.next;
        }
    }
}
