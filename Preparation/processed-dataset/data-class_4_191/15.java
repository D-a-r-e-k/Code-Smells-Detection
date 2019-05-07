/**
   * This method closes the Folder.  If you open the Folder using
   * openFolder (which you should), then you should use this method
   * instead of calling getFolder.close().  If you don't, then the
   * FolderInfo will try to reopen the folder.
   */
public void closeFolder(boolean expunge, boolean closeDisplay) throws MessagingException {
    if (closeDisplay) {
        unloadAllMessages();
        if (getFolderDisplayUI() != null)
            getFolderDisplayUI().closeFolderDisplay();
        setFolderDisplayUI(null);
    }
    if (getFolderTracker() != null) {
        getFolderTracker().removeFolder(this);
        setFolderTracker(null);
    }
    if (isLoaded() && isValid()) {
        setStatus(CLOSED);
        try {
            folder.close(expunge);
        } catch (java.lang.IllegalStateException ise) {
            throw new MessagingException(ise.getMessage(), ise);
        }
    }
}
