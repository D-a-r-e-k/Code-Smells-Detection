private synchronized Object remove(Object key, boolean invokeAlgorithm, boolean forcePersist) /* Previous code
    public Object remove(Object key) */
/** OpenSymphony END */
{
    /*
          Strategy:

          Find the entry, then
            1. Set value field to null, to force get() to retry
            2. Rebuild the list without this entry.
               All entries following removed node can stay in list, but
               all preceeding ones need to be cloned.  Traversals rely
               on this strategy to ensure that elements will not be
              repeated during iteration.
        */
    /** OpenSymphony BEGIN */
    if (key == null) {
        return null;
    }
    /** OpenSymphony END */
    int hash = hash(key);
    Entry[] tab = table;
    int index = hash & (tab.length - 1);
    Entry first = tab[index];
    Entry e = first;
    for (; ; ) {
        if (e == null) {
            tab = getTableForReading();
            if (first == tab[index]) {
                return null;
            } else {
                // Wrong list -- must restart traversal at new first  
                /** OpenSymphony BEGIN */
                /* Previous Code
                    return sremove(key, hash); */
                return sremove(key, hash, invokeAlgorithm);
            }
        } else if ((key == e.key) || ((e.hash == hash) && key.equals(e.key))) {
            synchronized (this) {
                tab = table;
                Object oldValue = e.value;
                if (persistenceListener != null && (oldValue == NULL)) {
                    oldValue = persistRetrieve(key);
                }
                // re-find under synch if wrong list  
                if ((first != tab[index]) || (oldValue == null)) {
                    /** OpenSymphony BEGIN */
                    /* Previous Code
                        return sremove(key, hash); */
                    return sremove(key, hash, invokeAlgorithm);
                }
                /** OpenSymphony END */
                e.value = null;
                count--;
                /** OpenSymphony BEGIN */
                if (forcePersist || (!unlimitedDiskCache && !overflowPersistence)) {
                    persistRemove(e.key);
                    // If we have a CacheEntry, update the group lookups  
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
                if (!forcePersist && overflowPersistence && ((size() + 1) >= maxEntries)) {
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
                // introduced to fix bug CACHE-255   
                if (oldValue instanceof CacheEntry) {
                    CacheEntry oldEntry = (CacheEntry) oldValue;
                    oldValue = oldEntry.getContent();
                }
                /** OpenSymphony END */
                Entry head = e.next;
                for (Entry p = first; p != e; p = p.next) {
                    head = new Entry(p.hash, p.key, p.value, head);
                }
                tab[index] = head;
                recordModification(head);
                return oldValue;
            }
        } else {
            e = e.next;
        }
    }
}
