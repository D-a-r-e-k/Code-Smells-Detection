protected void initCache() {
    // Initialize the file cache from FileStorage 
    super.initCache();
    // Now initialize the user cache 
    cs.configRegisterIntegerKey(this, "CACHE SIZE USER", "100", "Size of the user cache");
    try {
        // Default value 100, if parsing fails. 
        user_cache_size = 100;
        user_cache_size = Integer.parseInt(getConfig("CACHE SIZE USER"));
    } catch (NumberFormatException e) {
    }
    if (user_cache == null) {
        user_cache = new ExpireableCache(user_cache_size);
    } else {
        user_cache.setCapacity(user_cache_size);
    }
}
