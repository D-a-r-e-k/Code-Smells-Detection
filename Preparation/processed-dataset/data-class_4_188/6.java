/**
   * Called when the folder is closed.
   */
public void closed(ConnectionEvent e) {
    super.closed(e);
    rematchFilters();
}
