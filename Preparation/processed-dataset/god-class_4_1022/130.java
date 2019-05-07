/**
   * Sets this as an Outbox for the given OutgoingMailServer.  If this
   * is getting removed as an outbox, set the server to null.
   */
public void setOutboxFolder(OutgoingMailServer newServer) {
    mailServer = newServer;
    if (newServer != null) {
        setNotifyNewMessagesMain(false);
        setNotifyNewMessagesNode(false);
    } else {
        if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesMain", "").equalsIgnoreCase("false"))
            setNotifyNewMessagesMain(true);
        else
            setNotifyNewMessagesMain(false);
        if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesNode", "").equalsIgnoreCase("false"))
            setNotifyNewMessagesNode(true);
        else
            setNotifyNewMessagesNode(false);
    }
    resetDefaultActions();
}
