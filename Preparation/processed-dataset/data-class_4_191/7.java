public void disconnected(ConnectionEvent e) {
    synchronized (this) {
        if (getLogger().isLoggable(Level.FINE)) {
            folderLog(Level.FINE, "Folder " + getFolderID() + " disconnected.");
            Thread.dumpStack();
        }
        // if this happened accidentally, check it. 
        if (getStatus() != CLOSED) {
            getFolderThread().addToQueue(new javax.swing.AbstractAction() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // check to see if the parent store is still open. 
                    StoreInfo parentStoreInfo = getParentStore();
                    if (parentStoreInfo != null && parentStoreInfo.isConnected()) {
                        parentStoreInfo.checkConnection();
                    }
                }
            }, new java.awt.event.ActionEvent(this, 0, "folder-closed"), ActionThread.PRIORITY_HIGH);
            if (getFolderDisplayUI() != null) {
                getFolderDisplayUI().showStatusMessage(Pooka.getProperty("error.UIDFolder.disconnected", "Lost connection to folder..."));
            }
            if (status == CONNECTED) {
                setStatus(LOST_CONNECTION);
            }
        }
    }
    fireConnectionEvent(e);
}
