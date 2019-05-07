/**
   * Called when the status of the NetworkConnection changes.
   */
public void connectionStatusChanged(NetworkConnection connection, int newStatus) {
    // mbox folders still don't care. 
    if (!(protocol.equalsIgnoreCase("mbox") || protocol.equalsIgnoreCase("maildir"))) {
        if (newStatus == NetworkConnection.CONNECTED) {
        } else if (newStatus == NetworkConnection.DISCONNECTED) {
            // we're being disconnected.  close all the connections. 
            try {
                disconnectStore();
            } catch (MessagingException me) {
                getLogger().log(Level.FINE, "Caught exception disconnecting Store " + getStoreID() + ":  " + me);
                if (getLogger().isLoggable(Level.FINE))
                    me.printStackTrace();
            }
        } else {
            // we've been cut off.  note it. 
            try {
                disconnectStore();
            } catch (MessagingException me) {
                getLogger().log(Level.FINE, "Caught exception disconnecting Store " + getStoreID() + ":  " + me);
                if (getLogger().isLoggable(Level.FINE))
                    me.printStackTrace();
            }
        }
    }
}
