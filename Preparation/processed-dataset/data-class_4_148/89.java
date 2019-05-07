/**
     * <p>
     * Get the number of <code>{@link org.quartz.Job}</code> s that are
     * stored in the <code>JobStore</code>.
     * </p>
     */
public int getNumberOfJobs(final SchedulingContext ctxt) throws JobPersistenceException {
    return ((Integer) executeWithoutLock(// no locks necessary for read... 
    new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return new Integer(getNumberOfJobs(conn, ctxt));
        }
    })).intValue();
}
