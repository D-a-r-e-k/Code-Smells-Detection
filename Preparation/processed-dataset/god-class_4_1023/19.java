/**
   * Tests the NetworkConnection status for this store.
   */
private void testConnection() throws MessagingException {
    // don't test for connections for mbox providers. 
    if (!(protocol.equalsIgnoreCase("mbox") || protocol.equalsIgnoreCase("maildir"))) {
        NetworkConnection currentConnection = getConnection();
        getLogger().log(Level.FINE, "connect store " + getStoreID() + ":  checking connection.");
        if (currentConnection != null) {
            if (currentConnection.getStatus() == NetworkConnection.DISCONNECTED) {
                getLogger().log(Level.FINE, "connect store " + getStoreID() + ":  connection not up.  trying to connect it..");
                currentConnection.connect(true, true);
            }
            if (connection.getStatus() != NetworkConnection.CONNECTED) {
                throw new MessagingException(Pooka.getProperty("error.connectionDown", "Connection down for Store:  ") + getItemID());
            } else {
                getLogger().log(Level.FINE, "connect store " + getStoreID() + ":  successfully opened connection.");
            }
        }
    }
}
