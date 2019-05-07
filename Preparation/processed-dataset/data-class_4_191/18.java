/**
   * Gets all of the children folders of this FolderInfo which are
   * both Open and can contain Messages.  The return value should include
   * the current FolderInfo, if it is Open and can contain Messages.
   */
public Vector getAllFolders() {
    Vector returnValue = new Vector();
    if (children != null) {
        for (int i = 0; i < children.size(); i++) returnValue.addAll(((FolderInfo) children.elementAt(i)).getAllFolders());
    }
    if (isSortaOpen() && (getType() & Folder.HOLDS_MESSAGES) != 0)
        returnValue.add(this);
    return returnValue;
}
