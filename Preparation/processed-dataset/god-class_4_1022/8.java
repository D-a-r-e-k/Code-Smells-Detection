/**
   * Invoked when a Store/Folder/Transport is opened.
   *
   * As specified in javax.mail.event.ConnectionListener.
   */
public void opened(ConnectionEvent e) {
    fireConnectionEvent(e);
}
