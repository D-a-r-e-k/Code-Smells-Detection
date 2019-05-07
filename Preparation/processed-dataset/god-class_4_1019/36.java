/**
   * Returns the cache directory for this FolderInfo.
   */
public String getCacheDirectory() {
    String localDir = Pooka.getResourceManager().translateName(Pooka.getProperty(getFolderProperty() + ".cacheDir", ""));
    if (!localDir.equals(""))
        return localDir;
    localDir = Pooka.getProperty("Pooka.defaultMailSubDir", "");
    if (localDir.equals(""))
        localDir = Pooka.getPookaManager().getPookaRoot().getAbsolutePath() + File.separator + ".pooka";
    localDir = Pooka.getResourceManager().translateName(localDir);
    localDir = localDir + File.separatorChar + "cache";
    FolderInfo currentFolder = this;
    StringBuffer subDir = new StringBuffer();
    subDir.insert(0, currentFolder.getFolderName());
    subDir.insert(0, File.separatorChar);
    while (currentFolder.getParentFolder() != null) {
        currentFolder = currentFolder.getParentFolder();
        subDir.insert(0, currentFolder.getFolderName());
        subDir.insert(0, File.separatorChar);
    }
    subDir.insert(0, currentFolder.getParentStore().getStoreID());
    subDir.insert(0, File.separatorChar);
    return localDir + subDir.toString();
}
