/**
     * <p>
     * Get the names of all of the <code>{@link org.quartz.Calendar}</code> s
     * in the <code>JobStore</code>.
     * </p>
     * 
     * <p>
     * If there are no Calendars in the given group name, the result should be
     * a zero-length array (not <code>null</code>).
     * </p>
     */
public String[] getCalendarNames(final SchedulingContext ctxt) throws JobPersistenceException {
    return (String[]) executeWithoutLock(// no locks necessary for read... 
    new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return getCalendarNames(conn, ctxt);
        }
    });
}
