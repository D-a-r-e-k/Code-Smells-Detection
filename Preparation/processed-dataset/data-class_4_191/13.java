/**
   * This method calls openFolder() on this FolderInfo, and then, if
   * this FolderInfo has any children, calls openFolder() on them,
   * also.
   *
   * This is usually called by StoreInfo.connectStore() if
   * Pooka.openFoldersOnConnect is set to true.
   */
public void openAllFolders(int mode) {
    try {
        openFolder(mode, false);
    } catch (MessagingException me) {
    } catch (OperationCancelledException oce) {
    }
    if (children != null) {
        for (int i = 0; i < children.size(); i++) {
            doOpenFolders((FolderInfo) children.elementAt(i), mode);
        }
    }
}
