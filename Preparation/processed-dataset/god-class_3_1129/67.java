public void setCacheManager(CacheManager value) {
    CacheManager mgr = getCacheManager();
    if (mgr != null) {
        log.warn("Existing CacheManager " + mgr.getName() + " superseded by " + value.getName());
    }
    setCacheManagerProperty(value);
}
