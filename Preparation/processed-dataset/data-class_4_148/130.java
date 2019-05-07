protected void releaseAcquiredTrigger(Connection conn, SchedulingContext ctxt, Trigger trigger) throws JobPersistenceException {
    try {
        getDelegate().updateTriggerStateFromOtherState(conn, trigger.getName(), trigger.getGroup(), STATE_WAITING, STATE_ACQUIRED);
        getDelegate().deleteFiredTrigger(conn, trigger.getFireInstanceId());
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't release acquired trigger: " + e.getMessage(), e);
    }
}
