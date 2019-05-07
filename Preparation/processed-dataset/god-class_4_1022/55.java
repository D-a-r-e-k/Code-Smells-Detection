/**
   * This subscribes to the FolderInfo indicated by the given String.
   * If this defines a subfolder, then that subfolder is added to this
   * FolderInfo, if it doesn't already exist.
   */
public void subscribeFolder(String folderName) {
    folderLog(Level.FINE, "Folder " + getFolderID() + " subscribing subfolder " + folderName);
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
    folderLog(Level.FINE, "Folder " + getFolderID() + " subscribing folder " + childFolderName + ", plus subfolder " + subFolderName);
    this.addToFolderList(childFolderName);
    FolderInfo childFolder = getChild(childFolderName);
    folderLog(Level.FINE, "got child folder " + childFolder + " from childFolderName " + childFolderName);
    if (childFolder != null && subFolderName != null) {
        childFolder.subscribeFolder(subFolderName);
    }
    try {
        if (childFolder != null && childFolder.isLoaded() == false)
            childFolder.loadFolder();
    } catch (MessagingException me) {
        // if we get an exception loading a child folder while subscribing a 
        // folder object, just ignore it. 
        folderLog(Level.FINE, Thread.currentThread() + "loading folder " + getFolderID() + ":  caught messaging exception from parentStore getting folder: " + me);
        if (getLogger().isLoggable(Level.FINE))
            me.printStackTrace();
    } catch (OperationCancelledException me) {
    }
    updateChildren();
}
