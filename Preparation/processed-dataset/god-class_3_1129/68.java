public CacheManager getCacheManager() {
    return (CacheManager) getProperty(CACHE_MANAGER).getObjectValue();
}
