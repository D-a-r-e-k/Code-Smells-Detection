/**
     * <p>
     * Inform the <code>JobStore</code> that the scheduler no longer plans to
     * fire the given <code>Trigger</code>, that it had previously acquired
     * (reserved).
     * </p>
     */
public void releaseAcquiredTrigger(final SchedulingContext ctxt, final Trigger trigger) throws JobPersistenceException {
    executeInNonManagedTXLock(LOCK_TRIGGER_ACCESS, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            releaseAcquiredTrigger(conn, ctxt, trigger);
        }
    });
}
