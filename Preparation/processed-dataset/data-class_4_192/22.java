/**
   * Opens the given folders in the UI.
   */
public void openFolders(List<FolderInfo> folderList) {
    try {
        connectStore();
        for (FolderInfo fInfo : folderList) {
            final FolderNode fNode = fInfo.getFolderNode();
            fNode.openFolder(false, false);
        }
    } catch (MessagingException me) {
        // on failure still open caching folders. 
        boolean showError = false;
        for (FolderInfo fInfo : folderList) {
            if (fInfo instanceof net.suberic.pooka.cache.CachingFolderInfo) {
                if (!(((net.suberic.pooka.cache.CachingFolderInfo) fInfo).getCacheHeadersOnly())) {
                    final FolderNode fNode = fInfo.getFolderNode();
                    fNode.openFolder(false, false);
                }
            } else {
                showError = true;
            }
        }
        if (showError) {
            Pooka.getUIFactory().showError(Pooka.getResources().formatMessage("error.Store.connecton.failed", getStoreID()), me);
        }
    } catch (OperationCancelledException oce) {
        // on failure still open caching folders. 
        for (FolderInfo fInfo : folderList) {
            if (fInfo instanceof net.suberic.pooka.cache.CachingFolderInfo) {
                if (!(((net.suberic.pooka.cache.CachingFolderInfo) fInfo).getCacheHeadersOnly())) {
                    final FolderNode fNode = fInfo.getFolderNode();
                    fNode.openFolder(false, false);
                }
            }
        }
    }
}
