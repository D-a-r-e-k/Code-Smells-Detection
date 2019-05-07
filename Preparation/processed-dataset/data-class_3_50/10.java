/**
     * Handles the event fired when an entry is removed from the cache.
     *
     * @param event The event triggered when a cache entry has been removed
     */
public void cacheEntryRemoved(CacheEntryEvent event) {
    entryRemovedCount++;
}
