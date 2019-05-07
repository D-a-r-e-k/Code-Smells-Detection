/**
     * Gets the connection manager, the means by which the database is accessed.  If no connection has
     * yet been set up, a dialogue will be displayed to the user with the DB settings and following this,
     * a connection is attempted.
     *
     * @return Value of property conManager.
     */
public static GenericJdbcManager getConManager() {
    if (conManager == null) {
        JDialog dialog = new ConnectDialog(jagGenerator);
        dialog.setVisible(true);
    }
    if (conManager != null) {
        jagGenerator.disconnectMenuItem.setEnabled(true);
    }
    return conManager;
}
