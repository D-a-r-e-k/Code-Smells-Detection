/**
   * This unsubscribes this FolderInfo and all of its children, if
   * applicable.
   *
   * This implementation just removes the defining properties from
   * the Pooka resources.
   */
public void unsubscribe() {
    cleanup();
    if (parentFolder != null)
        parentFolder.removeFromFolderList(getFolderName());
    else if (parentStore != null)
        parentStore.removeFromFolderList(getFolderName());
    try {
        if (folder != null)
            folder.setSubscribed(false);
    } catch (MessagingException me) {
        Pooka.getUIFactory().showError(Pooka.getProperty("error.folder.unsubscribe", "Error unsubscribing on server from folder ") + getFolderID(), me);
    }
}
