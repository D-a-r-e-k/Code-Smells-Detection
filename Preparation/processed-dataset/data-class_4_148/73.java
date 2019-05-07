/**
     * <p>
     * Remove (delete) the <code>{@link org.quartz.Trigger}</code> with the
     * given name.
     * </p>
     * 
     * <p>
     * If removal of the <code>Trigger</code> results in an empty group, the
     * group should be removed from the <code>JobStore</code>'s list of
     * known group names.
     * </p>
     * 
     * <p>
     * If removal of the <code>Trigger</code> results in an 'orphaned' <code>Job</code>
     * that is not 'durable', then the <code>Job</code> should be deleted
     * also.
     * </p>
     * 
     * @param triggerName
     *          The name of the <code>Trigger</code> to be removed.
     * @param groupName
     *          The group name of the <code>Trigger</code> to be removed.
     * @return <code>true</code> if a <code>Trigger</code> with the given
     *         name & group was found and removed from the store.
     */
public boolean removeTrigger(final SchedulingContext ctxt, final String triggerName, final String groupName) throws JobPersistenceException {
    return ((Boolean) executeInLock(LOCK_TRIGGER_ACCESS, new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return removeTrigger(conn, ctxt, triggerName, groupName) ? Boolean.TRUE : Boolean.FALSE;
        }
    })).booleanValue();
}
