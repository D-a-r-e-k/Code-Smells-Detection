protected Trigger retrieveTrigger(Connection conn, SchedulingContext ctxt, String triggerName, String groupName) throws JobPersistenceException {
    return retrieveTrigger(conn, triggerName, groupName);
}
