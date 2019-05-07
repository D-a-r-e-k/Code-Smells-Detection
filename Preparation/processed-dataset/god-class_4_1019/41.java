/**
   * Returns if we're caching only headers.
   */
public boolean getCacheHeadersOnly() {
    return Pooka.getProperty(getFolderProperty() + ".cacheHeadersOnly", Pooka.getProperty(getParentStore().getStoreProperty() + ".cacheHeadersOnly", "false")).equalsIgnoreCase("true");
}
