/**
   * This updates the children of the current folder.  Generally called
   * when the folderList property is changed.
   *
   * Should be called on the folder thread.
   */
public void updateChildren() {
    Vector<FolderInfo> newChildren = new Vector();
    List<String> newChildNames = Pooka.getResources().getPropertyAsList(getFolderProperty() + ".folderList", "");
    for (String newFolderName : newChildNames) {
        FolderInfo childFolder = getChild(newFolderName);
        if (childFolder == null) {
            childFolder = createChildFolder(newFolderName);
        }
        newChildren.add(0, childFolder);
        children = newChildren;
    }
    if (folderNode != null)
        folderNode.loadChildren();
}
