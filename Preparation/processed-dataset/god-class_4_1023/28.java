/**
   * Checks connection for this store.
   */
public boolean checkConnection() {
    // if we don't think we're connected, don't check. 
    if (!isConnected())
        return false;
    // don't check if we've checked very recently. 
    if (System.currentTimeMillis() - lastConnectionCheck > 20000) {
        getLogger().log(Level.FINER, "Checking connection for store " + getStoreID());
        Store realStore = getStore();
        if (realStore != null) {
            if (!realStore.isConnected()) {
                getLogger().log(Level.FINER, getStoreID() + ":  isConnected() returns false.  returning false.");
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    } else {
        return isConnected();
    }
}
