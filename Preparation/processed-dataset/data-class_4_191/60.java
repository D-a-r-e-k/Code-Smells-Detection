/**
   * Cleans up all references to this folder.
   */
protected void cleanup() {
    Pooka.getResources().removeValueChangeListener(this);
    Folder f = getFolder();
    if (f != null) {
        removeFolderListeners();
    }
    if (children != null && children.size() > 0) {
        for (int i = 0; i < children.size(); i++) ((FolderInfo) children.elementAt(i)).cleanup();
    }
    Pooka.getLogManager().removeLogger(getFolderProperty());
    if (getFolderDisplayUI() != null)
        getFolderDisplayUI().closeFolderDisplay();
}
