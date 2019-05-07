/**
   * called when the folder is opened.
   */
public void opened(ConnectionEvent e) {
    super.opened(e);
    rematchFilters();
}
