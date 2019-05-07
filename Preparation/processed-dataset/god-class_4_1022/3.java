/**
   * This adds this a listener to the Folder.
   */
protected void addFolderListeners() {
    if (folder != null) {
        folder.addMessageChangedListener(this);
        folder.addMessageCountListener(this);
        folder.addConnectionListener(this);
    }
}
