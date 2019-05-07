// private method to allow AsyncSample to reset the value without performing checks 
private void setCacheManagerProperty(CacheManager value) {
    setProperty(new TestElementProperty(CACHE_MANAGER, value));
}
