/**
     * <p>
     * Get the names of all of the <code>{@link org.quartz.Job}</code>
     * groups.
     * </p>
     * 
     * <p>
     * If there are no known group names, the result should be a zero-length
     * array (not <code>null</code>).
     * </p>
     */
public String[] getJobGroupNames(final SchedulingContext ctxt) throws JobPersistenceException {
    return (String[]) executeWithoutLock(// no locks necessary for read... 
    new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return getJobGroupNames(conn, ctxt);
        }
    });
}
