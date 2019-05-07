/**
   * This returns whether or not this Folder is set up to use the
   * TrashFolder for the Store.  If this is a Trash Folder itself,
   * then return false.  If FolderProperty.useTrashFolder is set,
   * return that.  else go up the tree, until, in the end,
   * Pooka.useTrashFolder is returned.
   */
public boolean useTrashFolder() {
    if (isTrashFolder())
        return false;
    String prop = Pooka.getProperty(getFolderProperty() + ".useTrashFolder", "");
    if (!prop.equals(""))
        return (!prop.equalsIgnoreCase("false"));
    if (getParentFolder() != null)
        return getParentFolder().useTrashFolder();
    else if (getParentStore() != null)
        return getParentStore().useTrashFolder();
    else
        return (!Pooka.getProperty("Pooka.useTrashFolder", "true").equalsIgnoreCase("true"));
}
