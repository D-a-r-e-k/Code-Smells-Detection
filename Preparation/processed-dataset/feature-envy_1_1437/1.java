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
public void loadFolder(boolean pConnectStore) throws OperationCancelledException {
    if (cache == null) {
        try {
            this.cache = new SimpleFileCache(this, getCacheDirectory());
            type = type | Folder.HOLDS_MESSAGES;
            setStatus(DISCONNECTED);
        } catch (java.io.IOException ioe) {
            System.out.println("Error creating cache!");
            ioe.printStackTrace();
            return;
        }
    }
    if (isLoaded() || (loading && children == null))
        return;
    Folder[] tmpFolder = null;
    Folder tmpParentFolder;
    try {
        loading = true;
        if (getParentStore().isConnected()) {
            if (getParentFolder() == null) {
                try {
                    if (getLogger().isLoggable(Level.FINE))
                        getLogger().log(Level.FINE, Thread.currentThread() + "loading folder " + getFolderID() + ":  checking parent store connection.");
                    Store store = getParentStore().getStore();
                    // first see if we're a namespace 
                    try {
                        if (getLogger().isLoggable(Level.FINE)) {
                            getLogger().log(Level.FINE, "checking to see if " + getFolderID() + " is a shared folder.");
                        }
                        Folder[] sharedFolders = store.getSharedNamespaces();
                        if (sharedFolders != null && sharedFolders.length > 0) {
                            for (int i = 0; (tmpFolder == null || tmpFolder.length == 0) && i < sharedFolders.length; i++) {
                                if (sharedFolders[i].getName().equalsIgnoreCase(getFolderName())) {
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
                        if (getLogger().isLoggable(Level.FINE)) {
                            getLogger().log(Level.FINE, "got " + tmpParentFolder + " as Default Folder for store.");
                            getLogger().log(Level.FINE, "doing a list on default folder " + tmpParentFolder + " for folder " + getFolderName());
                        }
                        tmpFolder = tmpParentFolder.list(getFolderName());
                    }
                    if (getLogger().isLoggable(Level.FINE))
                        getLogger().log(Level.FINE, "got " + tmpFolder + " as Folder for folder " + getFolderID() + ".");
                } catch (MessagingException me) {
                    me.printStackTrace();
                    if (getLogger().isLoggable(Level.FINE)) {
                        getLogger().log(Level.FINE, Thread.currentThread() + "loading folder " + getFolderID() + ":  caught messaging exception from parentStore getting folder: " + me);
                        me.printStackTrace();
                    }
                    tmpFolder = null;
                }
            } else {
                if (!getParentFolder().isLoaded())
                    getParentFolder().loadFolder();
                if (!getParentFolder().isLoaded()) {
                    tmpFolder = null;
                } else {
                    tmpParentFolder = getParentFolder().getFolder();
                    if (tmpParentFolder != null) {
                        tmpFolder = tmpParentFolder.list(getFolderName());
                    } else {
                        tmpFolder = null;
                    }
                }
            }
            if (tmpFolder != null && tmpFolder.length > 0) {
                setFolder(tmpFolder[0]);
                if (!getFolder().isSubscribed())
                    getFolder().setSubscribed(true);
                setStatus(CLOSED);
                getFolder().addMessageChangedListener(this);
            } else {
                if (cache != null)
                    setStatus(CACHE_ONLY);
                else
                    setStatus(INVALID);
                setFolder(new FolderProxy(getFolderName()));
            }
        } else {
            setFolder(new FolderProxy(getFolderName()));
        }
    } catch (MessagingException me) {
        if (getLogger().isLoggable(Level.FINE)) {
            getLogger().log(Level.FINE, Thread.currentThread() + "loading folder " + getFolderID() + ":  caught messaging exception; setting loaded to false:  " + me.getMessage());
            me.printStackTrace();
        }
        setStatus(NOT_LOADED);
        setFolder(new FolderProxy(getFolderName()));
    } finally {
        initializeFolderInfo();
        loading = false;
    }
}
