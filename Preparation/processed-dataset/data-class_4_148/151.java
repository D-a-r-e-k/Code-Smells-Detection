/**
     * Rollback the supplied connection.
     * 
     * <p>  
     * Logs any SQLException it gets trying to rollback, but will not propogate
     * the exception lest it mask the exception that caused the caller to 
     * need to rollback in the first place.
     * </p>
     *
     * @param conn (Optional)
     */
protected void rollbackConnection(Connection conn) {
    if (conn != null) {
        try {
            conn.rollback();
        } catch (SQLException e) {
            getLog().error("Couldn't rollback jdbc connection. " + e.getMessage(), e);
        }
    }
}
