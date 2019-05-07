/**
   * This updates the children of the current store.  Generally called
   * when the folderList property is changed.
   */
public void updateChildren() {
    Vector<FolderInfo> newChildren = new Vector<FolderInfo>();
    List<String> newChildNames = Pooka.getResources().getPropertyAsList(getStoreProperty() + ".folderList", "INBOX");
    for (String newFolderName : newChildNames) {
        FolderInfo childFolder = getChild(newFolderName);
        if (childFolder == null) {
            childFolder = Pooka.getResourceManager().createFolderInfo(this, newFolderName);
        }
        newChildren.add(0, childFolder);
    }
    children = newChildren;
    getLogger().log(Level.FINE, getStoreID() + ":  in configureStore.  children.size() = " + children.size());
    if (storeNode != null)
        storeNode.loadChildren();
}
