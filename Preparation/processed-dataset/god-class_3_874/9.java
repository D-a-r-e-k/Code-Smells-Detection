/**
     * Handles the event fired when an entry is flushed from the cache.
     *
     * @param event The event triggered when a cache entry has been flushed
     */
public void cacheEntryFlushed(CacheEntryEvent event) {
    entryFlushedCount++;
}
