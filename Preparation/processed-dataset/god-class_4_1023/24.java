/**
   * Closes all of the Store's children.
   */
public void closeAllFolders(boolean expunge, boolean shuttingDown) throws MessagingException {
    if (getStoreThread() != null && !getStoreThread().getStopped()) {
        // if the store thread has exited, assume we're exiting, too. 
        synchronized (getStoreThread().getRunLock()) {
            getLogger().log(Level.FINE, "closing all folders of store " + getStoreID());
            Vector folders = getChildren();
            if (folders != null) {
                for (int i = 0; i < folders.size(); i++) {
                    ((FolderInfo) folders.elementAt(i)).closeAllFolders(expunge, shuttingDown);
                }
            }
        }
    }
}
