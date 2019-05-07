/**
     * Delete a job and its listeners.
     * 
     * @see #removeJob(Connection, SchedulingContext, String, String, boolean)
     * @see #removeTrigger(Connection, SchedulingContext, String, String)
     */
private boolean deleteJobAndChildren(Connection conn, SchedulingContext ctxt, String jobName, String groupName) throws NoSuchDelegateException, SQLException {
    getDelegate().deleteJobListeners(conn, jobName, groupName);
    return (getDelegate().deleteJobDetail(conn, jobName, groupName) > 0);
}
