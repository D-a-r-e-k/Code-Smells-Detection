/**
   * Creates a child folder.
   */
protected FolderInfo createChildFolder(String newFolderName) {
    if (!Pooka.getProperty(getFolderProperty() + "." + newFolderName + ".cacheMessages", "true").equalsIgnoreCase("false")) {
        return new CachingFolderInfo(this, newFolderName);
    } else if (Pooka.getProperty(getParentStore().getStoreProperty() + ".protocol", "mbox").equalsIgnoreCase("imap")) {
        return new UIDFolderInfo(this, newFolderName);
    } else {
        return new FolderInfo(this, newFolderName);
    }
}
