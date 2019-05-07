/**
   * This actually loads up the Folder object itself.  This is used so
   * that we can have a FolderInfo even if we're not connected to the
   * parent Store.
   *
   * Before we load the folder, the FolderInfo has the state of NOT_LOADED.
   * Once the parent store is connected, we can try to load the folder.
   * If the load is successful, we go to a CLOSED state.  If it isn't,
   * then we can either return to NOT_LOADED, or INVALID.
   */
public void loadFolder(boolean pConnectStore) throws MessagingException, OperationCancelledException {
    boolean parentIsConnected = false;
    if (isLoaded() || (loading && children == null))
        return;
    Folder[] tmpFolder = null;
    Folder tmpParentFolder;
    try {
        loading = true;
        if (parentStore != null) {
            folderLog(Level.FINE, Thread.currentThread() + "loading folder " + getFolderID() + ":  checking parent store connection.");
            if (!parentStore.isAvailable())
                throw new MessagingException();
            if (!parentStore.isConnected()) {
                if (pConnectStore) {
                    parentStore.connectStore();
                } else {
                    return;
                }
            }
            Store store = parentStore.getStore();
            // first see if we're a namespace 
            try {
                folderLog(Level.FINE, "checking to see if " + getFolderID() + " is a shared folder.");
                Folder[] sharedFolders = store.getSharedNamespaces();
                if (sharedFolders != null && sharedFolders.length > 0) {
                    for (int i = 0; (tmpFolder == null || tmpFolder.length == 0) && i < sharedFolders.length; i++) {
                        if (sharedFolders[i].getName().equalsIgnoreCase(mFolderName)) {
                            if (!mNamespace) {
                                Pooka.setProperty(getFolderID() + "._namespace", "true");
                                mNamespace = true;
                            }
                            tmpFolder = new Folder[1];
                            tmpFolder[0] = sharedFolders[i];
                        }
                    }
                }
            } catch (Exception e) {
            }
            if (tmpFolder == null || tmpFolder.length == 0) {
                // not a shared namespace 
                tmpParentFolder = store.getDefaultFolder();
                folderLog(Level.FINE, "got " + tmpParentFolder + " as Default Folder for store.");
                folderLog(Level.FINE, "doing a list on default folder " + tmpParentFolder + " for folder " + mFolderName);
                tmpFolder = tmpParentFolder.list(mFolderName);
            }
            folderLog(Level.FINE, "got " + tmpFolder + " as Folder for folder " + getFolderID() + ".");
        } else {
            if (!parentFolder.isLoaded())
                parentFolder.loadFolder();
            if (!parentFolder.isLoaded()) {
                tmpFolder = null;
            } else {
                tmpParentFolder = parentFolder.getFolder();
                if (tmpParentFolder != null) {
                    parentIsConnected = true;
                    folderLog(Level.FINE, "running list (" + mFolderName + ") on parent folder " + tmpParentFolder);
                    tmpFolder = tmpParentFolder.list(mFolderName);
                } else {
                    tmpFolder = null;
                }
            }
        }
        if (tmpFolder != null && tmpFolder.length > 0) {
            setFolder(tmpFolder[0]);
            if (!getFolder().isSubscribed())
                getFolder().setSubscribed(true);
            type = getFolder().getType();
            setStatus(CLOSED);
        } else {
            folderLog(Level.FINE, "folder " + mFolderName + " does not exist; setting as INVALID.");
            if (parentIsConnected)
                setStatus(INVALID);
            setFolder(null);
        }
    } finally {
        loading = false;
    }
    initializeFolderInfo();
}
