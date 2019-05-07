/**
   * This goes through the list of children of this store and
   * returns the FolderInfo for the given childName, if one exists.
   * If none exists, or if the children Vector has not been loaded
   * yet, or if this is a leaf node, then this method returns null.
   */
public FolderInfo getChild(String childName) {
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
        for (int i = 0; i < children.size(); i++) if (((FolderInfo) children.elementAt(i)).getFolderName().equals(folderName))
            childFolder = (FolderInfo) children.elementAt(i);
    }
    if (childFolder != null && subFolderName != null)
        return childFolder.getChild(subFolderName);
    else
        return childFolder;
}
