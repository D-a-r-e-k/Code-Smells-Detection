/**
     * Delete a trigger, its listeners, and its Simple/Cron/BLOB sub-table entry.
     * 
     * @see #removeJob(Connection, SchedulingContext, String, String, boolean)
     * @see #removeTrigger(Connection, SchedulingContext, String, String)
     * @see #replaceTrigger(Connection, SchedulingContext, String, String, Trigger)
     */
private boolean deleteTriggerAndChildren(Connection conn, String triggerName, String triggerGroupName) throws SQLException, NoSuchDelegateException {
    DriverDelegate delegate = getDelegate();
    // Once it succeeds in deleting one sub-table entry it will not try the others. 
    if ((delegate.deleteSimpleTrigger(conn, triggerName, triggerGroupName) == 0) && (delegate.deleteCronTrigger(conn, triggerName, triggerGroupName) == 0)) {
        delegate.deleteBlobTrigger(conn, triggerName, triggerGroupName);
    }
    delegate.deleteTriggerListeners(conn, triggerName, triggerGroupName);
    return (delegate.deleteTrigger(conn, triggerName, triggerGroupName) > 0);
}
