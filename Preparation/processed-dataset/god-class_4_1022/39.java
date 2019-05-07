/**
   * This goes through the list of children of this folder and
   * returns the FolderInfo for the given childName, if one exists.
   * If none exists, or if the children Vector has not been loaded
   * yet, or if this is a leaf node, then this method returns null.
   */
public FolderInfo getChild(String childName) {
    folderLog(Level.FINE, "folder " + getFolderID() + " getting child " + childName);
    FolderInfo childFolder = null;
    String folderName = null, subFolderName = null;
    if (children != null) {
        int divider = childName.indexOf('/');
        if (divider > 0) {
            folderName = childName.substring(0, divider);
            if (divider < childName.length() - 1)
                subFolderName = childName.substring(divider + 1);
        } else
            folderName = childName;
        folderLog(Level.FINE, "getting direct child " + folderName);
        for (int i = 0; i < children.size(); i++) if (((FolderInfo) children.elementAt(i)).getFolderName().equals(folderName))
            childFolder = (FolderInfo) children.elementAt(i);
    } else {
        folderLog(Level.FINE, "children of " + getFolderID() + " is null.");
    }
    if (childFolder != null && subFolderName != null)
        return childFolder.getChild(subFolderName);
    else
        return childFolder;
}
