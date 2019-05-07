/**
     * Notify any underlying algorithm that an item has been retrieved from the cache.
     *
     * @param key The cache key of the item that was retrieved.
     */
protected abstract void itemRetrieved(Object key);
