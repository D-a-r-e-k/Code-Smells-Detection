/**
   * This returns the property which defines this FolderInfo, such as
   * "Store.myStore.INBOX".
   */
public String getFolderProperty() {
    return "Store." + getFolderID();
}
