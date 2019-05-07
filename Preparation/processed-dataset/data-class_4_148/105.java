/**
     * <p>
     * Get all of the Triggers that are associated to the given Job.
     * </p>
     * 
     * <p>
     * If there are no matches, a zero-length array should be returned.
     * </p>
     */
public Trigger[] getTriggersForJob(final SchedulingContext ctxt, final String jobName, final String groupName) throws JobPersistenceException {
    return (Trigger[]) executeWithoutLock(// no locks necessary for read... 
    new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return getTriggersForJob(conn, ctxt, jobName, groupName);
        }
    });
}
