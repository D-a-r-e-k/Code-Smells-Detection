public void closed(ConnectionEvent e) {
    synchronized (this) {
        folderLog(Level.FINE, "Folder " + getFolderID() + " closed:  " + e);
        // if this happened accidentally, check it. 
        if (getStatus() != CLOSED && getStatus() != DISCONNECTED) {
            getFolderThread().addToQueue(new javax.swing.AbstractAction() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // check to see if the parent store is still open. 
                    StoreInfo parentStoreInfo = getParentStore();
                    if (parentStoreInfo != null) {
                        if (parentStoreInfo.isConnected())
                            parentStoreInfo.checkConnection();
                    }
                }
            }, new java.awt.event.ActionEvent(this, 0, "folder-closed"), ActionThread.PRIORITY_HIGH);
            if (getFolderDisplayUI() != null) {
                getFolderDisplayUI().showStatusMessage(Pooka.getProperty(disconnectedMessage, "Lost connection to folder..."));
            }
            if (status == CONNECTED) {
                setStatus(LOST_CONNECTION);
            }
        }
    }
    fireConnectionEvent(e);
}
