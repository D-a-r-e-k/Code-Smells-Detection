/**
   * Cleans up all references to this StoreInfo.
   */
public void cleanup() {
    Pooka.getResources().removeValueChangeListener(this);
    Pooka.getLogManager().removeLogger(getStoreProperty());
    if (children != null && children.size() > 0) {
        for (int i = 0; i < children.size(); i++) ((FolderInfo) children.elementAt(i)).cleanup();
    }
    if (store != null) {
        store.removeConnectionListener(connectionListener);
    }
    if (getStoreThread() != null) {
        getStoreThread().setStop(true);
    }
}
