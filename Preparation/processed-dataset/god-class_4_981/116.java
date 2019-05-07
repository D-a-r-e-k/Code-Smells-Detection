/**
     * <p>
     * Pause all of the <code>{@link org.quartz.Trigger}s</code> in the
     * given group.
     * </p>
     * 
     * @see #resumeTriggerGroup(SchedulingContext, String)
     */
public void pauseTriggerGroup(final SchedulingContext ctxt, final String groupName) throws JobPersistenceException {
    executeInLock(LOCK_TRIGGER_ACCESS, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            pauseTriggerGroup(conn, ctxt, groupName);
        }
    });
}
