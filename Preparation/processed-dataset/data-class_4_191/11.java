/**
   * This method opens the Folder, and sets the FolderInfo to know that
   * the Folder should be open.  You should use this method instead of
   * calling getFolder().open(), because if you use this method, then
   * the FolderInfo will try to keep the Folder open, and will try to
   * reopen the Folder if it gets closed before closeFolder is called.
   *
   * This method can also be used to reset the mode of an already
   * opened folder.
   */
public void openFolder(int mode, boolean pConnectStore) throws MessagingException, OperationCancelledException {
    //System.err.println("timing:  opening folder " + getFolderID()); 
    //long currentTime = System.currentTimeMillis(); 
    folderLog(Level.FINE, this + ":  checking parent store.");
    if (!getParentStore().isConnected() && pConnectStore) {
        folderLog(Level.FINE, this + ":  parent store isn't connected.  trying connection.");
        getParentStore().connectStore();
    }
    folderLog(Level.FINE, this + ":  loading folder.");
    if (!isLoaded() && status != CACHE_ONLY)
        loadFolder(pConnectStore);
    folderLog(Level.FINE, this + ":  folder loaded.  status is " + status);
    folderLog(Level.FINE, this + ":  checked on parent store.  trying isLoaded() and isAvailable().");
    if (status == CLOSED || status == LOST_CONNECTION || status == DISCONNECTED) {
        folderLog(Level.FINE, this + ":  isLoaded() and isAvailable().");
        if (folder.isOpen()) {
            if (folder.getMode() == mode)
                return;
            else {
                folder.close(false);
                openFolder(mode);
                updateFolderOpenStatus(true);
                resetMessageCounts();
            }
        } else {
            folder.open(mode);
            updateFolderOpenStatus(true);
            resetMessageCounts();
        }
    } else if (status == INVALID) {
        throw new MessagingException(Pooka.getProperty("error.folderInvalid", "Error:  folder is invalid.  ") + getFolderID());
    }
}
