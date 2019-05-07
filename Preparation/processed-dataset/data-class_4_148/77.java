/**
     * <p>
     * Retrieve the given <code>{@link org.quartz.Trigger}</code>.
     * </p>
     * 
     * @param triggerName
     *          The name of the <code>Trigger</code> to be retrieved.
     * @param groupName
     *          The group name of the <code>Trigger</code> to be retrieved.
     * @return The desired <code>Trigger</code>, or null if there is no
     *         match.
     */
public Trigger retrieveTrigger(final SchedulingContext ctxt, final String triggerName, final String groupName) throws JobPersistenceException {
    return (Trigger) executeWithoutLock(// no locks necessary for read... 
    new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return retrieveTrigger(conn, ctxt, triggerName, groupName);
        }
    });
}
