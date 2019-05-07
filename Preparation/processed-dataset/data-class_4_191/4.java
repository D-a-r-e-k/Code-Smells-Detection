/**
   * This removes this as a listener to the Folder.
   */
protected void removeFolderListeners() {
    if (folder != null) {
        folder.removeMessageChangedListener(this);
        folder.removeMessageCountListener(this);
        folder.removeConnectionListener(this);
    }
}
