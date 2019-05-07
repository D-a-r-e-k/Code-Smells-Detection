/**
     * Get the pool size for concurrent thread pool to get embedded resources.
     *
     * @return the pool size
     */
public String getConcurrentPool() {
    return getPropertyAsString(CONCURRENT_POOL, CONCURRENT_POOL_DEFAULT);
}
