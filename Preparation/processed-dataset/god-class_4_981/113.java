/**
     * <p>
     * Resume (un-pause) the <code>{@link org.quartz.Trigger}</code> with the
     * given name.
     * </p>
     * 
     * <p>
     * If the <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseTrigger(Connection, SchedulingContext, String, String)
     */
public void resumeTrigger(Connection conn, SchedulingContext ctxt, String triggerName, String groupName) throws JobPersistenceException {
    try {
        TriggerStatus status = getDelegate().selectTriggerStatus(conn, triggerName, groupName);
        if (status == null || status.getNextFireTime() == null) {
            return;
        }
        boolean blocked = false;
        if (STATE_PAUSED_BLOCKED.equals(status.getStatus())) {
            blocked = true;
        }
        String newState = checkBlockedState(conn, ctxt, status.getJobKey().getName(), status.getJobKey().getGroup(), STATE_WAITING);
        boolean misfired = false;
        if (status.getNextFireTime().before(new Date())) {
            misfired = updateMisfiredTrigger(conn, ctxt, triggerName, groupName, newState, true);
        }
        if (!misfired) {
            if (blocked) {
                getDelegate().updateTriggerStateFromOtherState(conn, triggerName, groupName, newState, STATE_PAUSED_BLOCKED);
            } else {
                getDelegate().updateTriggerStateFromOtherState(conn, triggerName, groupName, newState, STATE_PAUSED);
            }
        }
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't resume trigger '" + groupName + "." + triggerName + "': " + e.getMessage(), e);
    }
}
