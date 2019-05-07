/**
     * <p>
     * Check existence of a given trigger.
     * </p>
     */
protected boolean triggerExists(Connection conn, String triggerName, String groupName) throws JobPersistenceException {
    try {
        return getDelegate().triggerExists(conn, triggerName, groupName);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't determine trigger existence (" + groupName + "." + triggerName + "): " + e.getMessage(), e);
    }
}
