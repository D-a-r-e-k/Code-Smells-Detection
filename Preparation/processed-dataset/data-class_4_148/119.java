/**
     * <p>
     * Pause all of the <code>{@link org.quartz.Trigger}s</code> in the
     * given group.
     * </p>
     * 
     * @see #resumeTriggerGroup(Connection, SchedulingContext, String)
     */
public Set getPausedTriggerGroups(Connection conn, SchedulingContext ctxt) throws JobPersistenceException {
    try {
        return getDelegate().selectPausedTriggerGroups(conn);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't determine paused trigger groups: " + e.getMessage(), e);
    }
}
