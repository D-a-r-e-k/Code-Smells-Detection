/**
     * Handles the event fired when a pattern is flushed from the cache.
     *
     * @param event The event triggered when a cache pattern has been flushed
     */
public void cachePatternFlushed(CachePatternEvent event) {
    patternFlushedCount++;
}
