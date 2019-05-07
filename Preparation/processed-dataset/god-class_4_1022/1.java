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
public void loadFolder() throws MessagingException, OperationCancelledException {
    loadFolder(true);
}
