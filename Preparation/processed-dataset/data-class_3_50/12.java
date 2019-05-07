/**
     * Handles the event fired when a group is flushed from the cache.
     *
     * @param event The event triggered when a cache group has been flushed
     */
public void cacheGroupFlushed(CacheGroupEvent event) {
    groupFlushedCount++;
}
