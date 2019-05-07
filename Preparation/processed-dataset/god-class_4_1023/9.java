/**
   * This goes through the list of children of this store and
   * returns the FolderInfo that matches this folderID.
   * If none exists, or if the children Vector has not been loaded
   * yet, or if this is a leaf node, then this method returns null.
   */
public FolderInfo getFolderById(String folderID) {
    FolderInfo childFolder = null;
    if (children != null) {
        for (int i = 0; i < children.size(); i++) {
            FolderInfo possibleMatch = ((FolderInfo) children.elementAt(i)).getFolderById(folderID);
            if (possibleMatch != null) {
                return possibleMatch;
            }
        }
    }
    return null;
}
