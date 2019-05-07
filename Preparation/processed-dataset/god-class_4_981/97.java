/**
     * <p>
     * Get the names of all of the <code>{@link org.quartz.Trigger}</code> s
     * that have the given group name.
     * </p>
     * 
     * <p>
     * If there are no triggers in the given group name, the result should be a
     * zero-length array (not <code>null</code>).
     * </p>
     */
public String[] getTriggerNames(final SchedulingContext ctxt, final String groupName) throws JobPersistenceException {
    return (String[]) executeWithoutLock(// no locks necessary for read... 
    new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return getTriggerNames(conn, ctxt, groupName);
        }
    });
}
