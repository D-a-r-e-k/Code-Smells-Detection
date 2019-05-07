/**
     * <p>
     * Check existence of a given job.
     * </p>
     */
protected boolean jobExists(Connection conn, String jobName, String groupName) throws JobPersistenceException {
    try {
        return getDelegate().jobExists(conn, jobName, groupName);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't determine job existence (" + groupName + "." + jobName + "): " + e.getMessage(), e);
    }
}
