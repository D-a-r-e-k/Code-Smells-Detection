/**
     * <p>
     * Resume (un-pause) all of the <code>{@link org.quartz.Trigger}s</code>
     * in the given group.
     * </p>
     * 
     * <p>
     * If any <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseTriggerGroup(SchedulingContext, String)
     */
public void resumeTriggerGroup(final SchedulingContext ctxt, final String groupName) throws JobPersistenceException {
    executeInLock(LOCK_TRIGGER_ACCESS, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            resumeTriggerGroup(conn, ctxt, groupName);
        }
    });
}
