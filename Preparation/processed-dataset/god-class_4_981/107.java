/**
     * <p>
     * Pause the <code>{@link org.quartz.Trigger}</code> with the given name.
     * </p>
     * 
     * @see #resumeTrigger(SchedulingContext, String, String)
     */
public void pauseTrigger(final SchedulingContext ctxt, final String triggerName, final String groupName) throws JobPersistenceException {
    executeInLock(LOCK_TRIGGER_ACCESS, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            pauseTrigger(conn, ctxt, triggerName, groupName);
        }
    });
}
