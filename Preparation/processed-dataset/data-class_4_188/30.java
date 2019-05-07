/**
   * This unsubscribes this FolderInfo and all of its children, if
   * applicable.
   *
   * For the CachingFolderInfo, this calls super.unsubscribe() and
   * getCache().invalidateCache().
   */
public void unsubscribe() {
    super.unsubscribe();
    getCache().invalidateCache();
}
