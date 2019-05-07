/**
   * Creates an appropriate FolderInfo for the given StoreInfo.
   */
public FolderInfo createFolderInfo(StoreInfo pStore, String pName) {
    String storeProperty = pStore.getStoreProperty();
    if (pStore.isPopStore() && pName.equalsIgnoreCase("INBOX")) {
        return new PopInboxFolderInfo(pStore, pName);
    } else if (Pooka.getProperty(storeProperty + ".protocol", "mbox").equalsIgnoreCase("imap")) {
        return new UIDFolderInfo(pStore, pName);
    } else {
        return new FolderInfo(pStore, pName);
    }
}
