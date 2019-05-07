/**
   * This method closes the Folder.  If you open the Folder using
   * openFolder (which you should), then you should use this method
   * instead of calling getFolder.close().  If you don't, then the
   * FolderInfo will try to reopen the folder.
   */
public void closeFolder(boolean expunge, boolean closeDisplay) throws MessagingException {
    if (closeDisplay && getFolderDisplayUI() != null)
        getFolderDisplayUI().closeFolderDisplay();
    if (isLoaded() && isAvailable()) {
        if (isConnected()) {
            try {
                getFolder().close(expunge);
            } catch (java.lang.IllegalStateException ise) {
                throw new MessagingException(ise.getMessage(), ise);
            }
        }
        if (getCache() != null) {
            setStatus(DISCONNECTED);
        } else {
            setStatus(CLOSED);
        }
    }
}
