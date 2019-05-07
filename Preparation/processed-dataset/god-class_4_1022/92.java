/**
   * This returns the folder display name, usually the FolderName plus
   * the store id.
   */
public String getFolderDisplayName() {
    return mFolderName + " - " + getParentStore().getStoreID();
}
