/**
     * Commit the supplied connection
     *
     * @param conn (Optional)
     * @throws JobPersistenceException thrown if a SQLException occurs when the
     * connection is committed
     */
protected void commitConnection(Connection conn) throws JobPersistenceException {
    if (conn != null) {
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't commit jdbc connection. " + e.getMessage(), e);
        }
    }
}
