/**
     * <p>
     * Get a handle to the next N triggers to be fired, and mark them as 'reserved'
     * by the calling scheduler.
     * </p>
     * 
     * @see #releaseAcquiredTrigger(SchedulingContext, Trigger)
     */
public Trigger acquireNextTrigger(final SchedulingContext ctxt, final long noLaterThan) throws JobPersistenceException {
    if (isAcquireTriggersWithinLock()) {
        // behavior before Quartz 1.6.3 release 
        return (Trigger) executeInNonManagedTXLock(LOCK_TRIGGER_ACCESS, new TransactionCallback() {

            public Object execute(Connection conn) throws JobPersistenceException {
                return acquireNextTrigger(conn, ctxt, noLaterThan);
            }
        });
    } else {
        // default behavior since Quartz 1.6.3 release 
        return (Trigger) executeInNonManagedTXLock(null, /* passing null as lock name causes no lock to be made */
        new TransactionCallback() {

            public Object execute(Connection conn) throws JobPersistenceException {
                return acquireNextTrigger(conn, ctxt, noLaterThan);
            }
        });
    }
}
