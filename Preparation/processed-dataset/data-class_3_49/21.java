/**
     * Returns the value to which the specified key is mapped in this table.
     *
     * @param   key   a key in the table.
     * @return  the value to which the key is mapped in this table;
     *          <code>null</code> if the key is not mapped to any value in
     *          this table.
     * @exception  NullPointerException  if the key is
     *               <code>null</code>.
     * @see     #put(Object, Object)
     */
public Object get(Object key) {
    if (log.isDebugEnabled()) {
        log.debug("get called (key=" + key + ")");
    }
    // throw null pointer exception if key null  
    int hash = hash(key);
    /*
           Start off at the apparently correct bin.  If entry is found, we
           need to check after a barrier anyway.  If not found, we need a
           barrier to check if we are actually in right bin. So either
           way, we encounter only one barrier unless we need to retry.
           And we only need to fully synchronize if there have been
           concurrent modifications.
        */
    Entry[] tab = table;
    int index = hash & (tab.length - 1);
    Entry first = tab[index];
    Entry e = first;
    for (; ; ) {
        if (e == null) {
            // If key apparently not there, check to  
            // make sure this was a valid read  
            tab = getTableForReading();
            if (first == tab[index]) {
                /** OpenSymphony BEGIN */
                /* Previous code
                    return null;*/
                // Not in the table, try persistence  
                Object value = persistRetrieve(key);
                if (value != null) {
                    // Update the map, but don't persist the data  
                    put(key, value, false);
                }
                return value;
            } else {
                // Wrong list -- must restart traversal at new first  
                e = first = tab[index = hash & (tab.length - 1)];
            }
        } else if ((key == e.key) || ((e.hash == hash) && key.equals(e.key))) {
            Object value = e.value;
            if (value != null) {
                /** OpenSymphony BEGIN */
                /* Previous code
                    return value;*/
                if (NULL.equals(value)) {
                    // Memory cache disable, use disk  
                    value = persistRetrieve(e.key);
                    if (value != null) {
                        itemRetrieved(key);
                    }
                    return value;
                } else {
                    itemRetrieved(key);
                    return value;
                }
            }
            // Entry was invalidated during deletion. But it could  
            // have been re-inserted, so we must retraverse.  
            // To avoid useless contention, get lock to wait out modifications  
            // before retraversing.  
            synchronized (this) {
                tab = table;
            }
            e = first = tab[index = hash & (tab.length - 1)];
        } else {
            e = e.next;
        }
    }
}
