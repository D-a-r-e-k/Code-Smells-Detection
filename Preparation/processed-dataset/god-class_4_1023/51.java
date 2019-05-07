/**
   * This returns whether or not this Store is set up to use the
   * TrashFolder.  If StoreProperty.useTrashFolder is set, return that as
   * a boolean.  Otherwise, return Pooka.useTrashFolder as a boolean.
   */
public boolean useTrashFolder() {
    if (getTrashFolder() == null)
        return false;
    String prop = Pooka.getProperty(getStoreProperty() + ".useTrashFolder", "");
    if (!prop.equals(""))
        return (!prop.equalsIgnoreCase("false"));
    else
        return (!Pooka.getProperty("Pooka.useTrashFolder", "true").equalsIgnoreCase("true"));
}
