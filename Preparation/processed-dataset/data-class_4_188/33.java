/**
   * Returns whether or not a given message is fully cached.
   */
public boolean isCached(long uid) {
    return getCache().isFullyCached(uid);
}
