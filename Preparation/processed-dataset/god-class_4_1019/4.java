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
    try {
        getLogger().log(Level.FINE, this + ":  checking parent store.");
        if (!getParentStore().isConnected() && pConnectStore && getParentStore().getPreferredStatus() == FolderInfo.CONNECTED) {
            getLogger().log(Level.FINE, this + ":  parent store isn't connected.  trying connection.");
            try {
                getParentStore().connectStore();
            } catch (OperationCancelledException oce) {
                getLogger().log(Level.INFO, "Login cancelled.");
            }
        }
        getLogger().log(Level.FINE, this + ":  loading folder.");
        if (!isLoaded() && status != CACHE_ONLY)
            loadFolder();
        getLogger().log(Level.FINE, this + ":  folder loaded.  status is " + status + "; checked on parent store.  trying isLoaded() and isAvailable().");
        if (status == CLOSED || status == LOST_CONNECTION || status == DISCONNECTED) {
            getLogger().log(Level.FINE, this + ":  isLoaded() and isAvailable().");
            if (getFolder().isOpen()) {
                if (getFolder().getMode() == mode)
                    return;
                else {
                    getFolder().close(false);
                    openFolder(mode);
                }
            } else {
                Folder f = getFolder();
                getFolder().open(mode);
                updateFolderOpenStatus(true);
                resetMessageCounts();
            }
        } else if (status == INVALID) {
            throw new MessagingException(Pooka.getProperty("error.folderInvalid", "Error:  folder is invalid.  ") + getFolderID());
        }
    } catch (MessagingException me) {
        setStatus(DISCONNECTED);
        throw me;
    } finally {
        resetMessageCounts();
    }
}
