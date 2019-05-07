/**
     * <p>
     * Pause the <code>{@link org.quartz.Trigger}</code> with the given name.
     * </p>
     * 
     * @see #resumeTrigger(Connection, SchedulingContext, String, String)
     */
public void pauseTrigger(Connection conn, SchedulingContext ctxt, String triggerName, String groupName) throws JobPersistenceException {
    try {
        String oldState = getDelegate().selectTriggerState(conn, triggerName, groupName);
        if (oldState.equals(STATE_WAITING) || oldState.equals(STATE_ACQUIRED)) {
            getDelegate().updateTriggerState(conn, triggerName, groupName, STATE_PAUSED);
        } else if (oldState.equals(STATE_BLOCKED)) {
            getDelegate().updateTriggerState(conn, triggerName, groupName, STATE_PAUSED_BLOCKED);
        }
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't pause trigger '" + groupName + "." + triggerName + "': " + e.getMessage(), e);
    }
}
