/**
   * This method disconnects the Store.  If you connect to the Store using
   * connectStore() (which you should), then you should use this method
   * instead of calling getStore.disconnect().  If you don't, then the
   * StoreInfo will try to reconnect the store.
   */
public void disconnectStore() throws MessagingException {
    getLogger().log(Level.FINE, "disconnecting store " + getStoreID());
    MessagingException storeException = null;
    if (!(store.isConnected())) {
        connected = false;
        closeAllFolders(false, false);
        return;
    } else {
        try {
            try {
                closeAllFolders(false, false);
            } catch (MessagingException folderMe) {
                storeException = folderMe;
            }
            store.close();
        } catch (MessagingException me) {
            if (storeException != null) {
                me.setNextException(storeException);
            }
            storeException = me;
            throw storeException;
        } finally {
            connected = false;
        }
        if (storeException != null)
            throw storeException;
    }
}
