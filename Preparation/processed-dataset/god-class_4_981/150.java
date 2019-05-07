/**
     * Closes the supplied <code>Connection</code>.
     * <p>
     * Ignores a <code>null Connection</code>.  
     * Any exception thrown trying to close the <code>Connection</code> is
     * logged and ignored.  
     * </p>
     * 
     * @param conn The <code>Connection</code> to close (Optional).
     */
protected void closeConnection(Connection conn) {
    if (conn != null) {
        try {
            conn.close();
        } catch (SQLException e) {
            getLog().error("Failed to close Connection", e);
        } catch (Throwable e) {
            getLog().error("Unexpected exception closing Connection." + "  This is often due to a Connection being returned after or during shutdown.", e);
        }
    }
}
