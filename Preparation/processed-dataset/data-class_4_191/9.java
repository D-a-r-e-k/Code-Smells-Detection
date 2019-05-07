/**
   * Invoked when a Store/Folder/Transport is opened.
   *
   * As specified in javax.mail.event.ConnectionListener.
   */
public void connected(ConnectionEvent e) {
    fireConnectionEvent(e);
}
