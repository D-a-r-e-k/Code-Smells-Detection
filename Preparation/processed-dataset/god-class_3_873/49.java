/**
     * Continuation of put(), called only when synch lock is
     * held and interference has been detected.
     **/
/** OpenSymphony BEGIN */
/* Previous code
    protected Object sput(Object key, Object value, int hash) {*/
protected Object sput(Object key, Object value, int hash, boolean persist) {
    /** OpenSymphony END */
    Entry[] tab = table;
    int index = hash & (tab.length - 1);
    Entry first = tab[index];
    Entry e = first;
    for (; ; ) {
        if (e == null) {
            /** OpenSymphony BEGIN */
            // Previous code  
            //  		Entry newEntry = new Entry(hash, key, value, first);  
            Entry newEntry;
            if (memoryCaching) {
                newEntry = new Entry(hash, key, value, first);
            } else {
                newEntry = new Entry(hash, key, NULL, first);
            }
            itemPut(key);
            // Persist if required  
            if (persist && !overflowPersistence) {
                persistStore(key, value);
            }
            // If we have a CacheEntry, update the group lookups  
            if (value instanceof CacheEntry) {
                updateGroups(null, (CacheEntry) value, persist);
            }
            /**        OpenSymphony END */
            tab[index] = newEntry;
            if (++count >= threshold) {
                rehash();
            } else {
                recordModification(newEntry);
            }
            return null;
        } else if ((key == e.key) || ((e.hash == hash) && key.equals(e.key))) {
            Object oldValue = e.value;
            /** OpenSymphony BEGIN */
            /* Previous code
                e.value = value; */
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
            /** OpenSymphony END */
            return oldValue;
        } else {
            e = e.next;
        }
    }
}
