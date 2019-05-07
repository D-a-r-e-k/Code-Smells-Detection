/**
   * Notifies the FolderNode that it needs to be updated.
   */
protected void updateNode() {
    if (getFolderNode() != null)
        getFolderNode().updateNode();
}
