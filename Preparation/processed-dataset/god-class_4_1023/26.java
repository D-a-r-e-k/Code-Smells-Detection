/**
   * Gets all of the children folders of this StoreInfo which are both
   * Open and can contain Messages.
   */
public Vector getAllFolders() {
    Vector returnValue = new Vector();
    Vector subFolders = getChildren();
    for (int i = 0; i < subFolders.size(); i++) {
        returnValue.addAll(((FolderInfo) subFolders.elementAt(i)).getAllFolders());
    }
    return returnValue;
}
