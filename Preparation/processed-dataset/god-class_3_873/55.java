/* Previous code
    public Object put(Object key, Object value)*/
private Object put(Object key, Object value, boolean persist) {
    /** OpenSymphony END */
    if (value == null) {
        throw new NullPointerException();
    }
    int hash = hash(key);
    Entry[] tab = table;
    int index = hash & (tab.length - 1);
    Entry first = tab[index];
    Entry e = first;
    for (; ; ) {
        if (e == null) {
            synchronized (this) {
                tab = table;
                /** OpenSymphony BEGIN */
                // Previous code  
                /*                                        if (first == tab[index]) {
                                                                    //  Add to front of list
                                                                    Entry newEntry = new Entry(hash, key, value, first);
                                                                    tab[index] = newEntry;
                                                                    if (++count >= threshold) rehash();
                                                                    else recordModification(newEntry);
                                                                    return null; */
                Object oldValue = null;
                // Remove an item if the cache is full  
                if (size() >= maxEntries) {
                    // part of fix CACHE-255: method should return old value  
                    oldValue = remove(removeItem(), false, false);
                }
                if (first == tab[index]) {
                    //  Add to front of list  
                    Entry newEntry = null;
                    if (memoryCaching) {
                        newEntry = new Entry(hash, key, value, first);
                    } else {
                        newEntry = new Entry(hash, key, NULL, first);
                    }
                    tab[index] = newEntry;
                    itemPut(key);
                    // Persist if required  
                    if (persist && !overflowPersistence) {
                        persistStore(key, value);
                    }
                    // If we have a CacheEntry, update the group lookups  
                    if (value instanceof CacheEntry) {
                        updateGroups(null, (CacheEntry) value, persist);
                    }
                    if (++count >= threshold) {
                        rehash();
                    } else {
                        recordModification(newEntry);
                    }
                    return oldValue;
                } else {
                    // wrong list -- retry  
                    /** OpenSymphony BEGIN */
                    /* Previous code
                        return sput(key, value, hash);*/
                    return sput(key, value, hash, persist);
                }
            }
        } else if ((key == e.key) || ((e.hash == hash) && key.equals(e.key))) {
            // synch to avoid race with remove and to  
            // ensure proper serialization of multiple replaces  
            synchronized (this) {
                tab = table;
                Object oldValue = e.value;
                // [CACHE-118] - get the old cache entry even if there's no memory cache  
                if (persist && (oldValue == NULL)) {
                    oldValue = persistRetrieve(key);
                }
                if ((first == tab[index]) && (oldValue != null)) {
                    /** OpenSymphony BEGIN */
                    /* Previous code
                        e.value = value;
                        return oldValue; */
                    if (memoryCaching) {
                        e.value = value;
                    }
                    // Persist if required  
                    if (persist && overflowPersistence) {
                        persistRemove(key);
                    } else if (persist) {
                        persistStore(key, value);
                    }
                    updateGroups(oldValue, value, persist);
                    itemPut(key);
                    return oldValue;
                } else {
                    // retry if wrong list or lost race against concurrent remove  
                    /** OpenSymphony BEGIN */
                    /* Previous code
                        return sput(key, value, hash);*/
                    return sput(key, value, hash, persist);
                }
            }
        } else {
            e = e.next;
        }
    }
}
