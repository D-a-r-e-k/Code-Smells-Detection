/**
     * Close the specified database connection.
     */
protected void close() {
    // Do nothing if the database connection is already closed 
    if (conn == null)
        return;
    // Close our prepared statements (if any) 
    try {
        ps.close();
    } catch (Throwable f) {
        ExceptionUtils.handleThrowable(f);
    }
    this.ps = null;
    // Close this database connection, and log any errors 
    try {
        conn.close();
    } catch (SQLException e) {
        container.getLogger().error(sm.getString("jdbcAccessLogValeve.close"), e);
    } finally {
        this.conn = null;
    }
}
