/**
     * <p>
     * Retrieve the given <code>{@link org.quartz.Trigger}</code>.
     * </p>
     * 
     * @param calName
     *          The name of the <code>Calendar</code> to be retrieved.
     * @return The desired <code>Calendar</code>, or null if there is no
     *         match.
     */
public Calendar retrieveCalendar(final SchedulingContext ctxt, final String calName) throws JobPersistenceException {
    return (Calendar) executeWithoutLock(// no locks necessary for read... 
    new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return retrieveCalendar(conn, ctxt, calName);
        }
    });
}
