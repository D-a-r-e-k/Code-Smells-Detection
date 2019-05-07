/**
   * Called when the store in disconnected.
   */
public void disconnected(ConnectionEvent e) {
    super.disconnected(e);
    rematchFilters();
}
