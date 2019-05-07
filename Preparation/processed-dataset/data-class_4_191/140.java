/**
   * This method actually returns the parent StoreInfo.  If this
   * particular FolderInfo is a child of another FolderInfo, this
   * method will call getParentStore() on that FolderInfo.
   */
public StoreInfo getParentStore() {
    if (parentStore == null)
        return parentFolder.getParentStore();
    else
        return parentStore;
}
