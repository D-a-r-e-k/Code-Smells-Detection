/**
     * <p>
     * Pause all of the <code>{@link org.quartz.Trigger}s</code> in the
     * given group.
     * </p>
     * 
     * @see #resumeTriggerGroup(Connection, SchedulingContext, String)
     */
public void pauseTriggerGroup(Connection conn, SchedulingContext ctxt, String groupName) throws JobPersistenceException {
    try {
        getDelegate().updateTriggerGroupStateFromOtherStates(conn, groupName, STATE_PAUSED, STATE_ACQUIRED, STATE_WAITING, STATE_WAITING);
        getDelegate().updateTriggerGroupStateFromOtherState(conn, groupName, STATE_PAUSED_BLOCKED, STATE_BLOCKED);
        if (!getDelegate().isTriggerGroupPaused(conn, groupName)) {
            getDelegate().insertPausedTriggerGroup(conn, groupName);
        }
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't pause trigger group '" + groupName + "': " + e.getMessage(), e);
    }
}
