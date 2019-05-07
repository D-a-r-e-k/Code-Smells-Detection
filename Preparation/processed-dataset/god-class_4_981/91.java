/**
     * <p>
     * Get the number of <code>{@link org.quartz.Trigger}</code> s that are
     * stored in the <code>JobsStore</code>.
     * </p>
     */
public int getNumberOfTriggers(final SchedulingContext ctxt) throws JobPersistenceException {
    return ((Integer) executeWithoutLock(// no locks necessary for read... 
    new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return new Integer(getNumberOfTriggers(conn, ctxt));
        }
    })).intValue();
}
