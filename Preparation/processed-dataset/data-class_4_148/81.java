public int getTriggerState(Connection conn, SchedulingContext ctxt, String triggerName, String groupName) throws JobPersistenceException {
    try {
        String ts = getDelegate().selectTriggerState(conn, triggerName, groupName);
        if (ts == null) {
            return Trigger.STATE_NONE;
        }
        if (ts.equals(STATE_DELETED)) {
            return Trigger.STATE_NONE;
        }
        if (ts.equals(STATE_COMPLETE)) {
            return Trigger.STATE_COMPLETE;
        }
        if (ts.equals(STATE_PAUSED)) {
            return Trigger.STATE_PAUSED;
        }
        if (ts.equals(STATE_PAUSED_BLOCKED)) {
            return Trigger.STATE_PAUSED;
        }
        if (ts.equals(STATE_ERROR)) {
            return Trigger.STATE_ERROR;
        }
        if (ts.equals(STATE_BLOCKED)) {
            return Trigger.STATE_BLOCKED;
        }
        return Trigger.STATE_NORMAL;
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't determine state of trigger (" + groupName + "." + triggerName + "): " + e.getMessage(), e);
    }
}
