/**
     * <p>
     * Store the given <code>{@link org.quartz.Trigger}</code>.
     * </p>
     * 
     * @param newTrigger
     *          The <code>Trigger</code> to be stored.
     * @param replaceExisting
     *          If <code>true</code>, any <code>Trigger</code> existing in
     *          the <code>JobStore</code> with the same name & group should
     *          be over-written.
     * @throws ObjectAlreadyExistsException
     *           if a <code>Trigger</code> with the same name/group already
     *           exists, and replaceExisting is set to false.
     */
public void storeTrigger(final SchedulingContext ctxt, final Trigger newTrigger, final boolean replaceExisting) throws ObjectAlreadyExistsException, JobPersistenceException {
    executeInLock((isLockOnInsert() || replaceExisting) ? LOCK_TRIGGER_ACCESS : null, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            storeTrigger(conn, ctxt, newTrigger, null, replaceExisting, STATE_WAITING, false, false);
        }
    });
}
