/**
   * This subscribes the Folder described by the given String to this
   * StoreInfo.
   */
public void subscribeFolder(String folderName) {
    getLogger().log(Level.FINE, "subscribing folder " + folderName);
    String subFolderName = null;
    String childFolderName = null;
    int firstSlash = folderName.indexOf('/');
    while (firstSlash == 0) {
        folderName = folderName.substring(1);
        firstSlash = folderName.indexOf('/');
    }
    if (firstSlash > 0) {
        childFolderName = folderName.substring(0, firstSlash);
        if (firstSlash < folderName.length() - 1)
            subFolderName = folderName.substring(firstSlash + 1);
    } else
        childFolderName = folderName;
    getLogger().log(Level.FINE, "store " + getStoreID() + " subscribing folder " + childFolderName + "; sending " + subFolderName + " to child for subscription.");
    this.addToFolderList(childFolderName);
    FolderInfo childFolder = getChild(childFolderName);
    getLogger().log(Level.FINE, "got child folder '" + childFolder + "' for " + childFolderName);
    if (childFolder != null && subFolderName != null) {
        childFolder.subscribeFolder(subFolderName);
    }
}
