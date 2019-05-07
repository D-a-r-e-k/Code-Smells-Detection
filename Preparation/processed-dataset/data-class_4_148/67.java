/**
     * <p>
     * Remove (delete) the <code>{@link org.quartz.Job}</code> with the given
     * name, and any <code>{@link org.quartz.Trigger}</code> s that reference
     * it.
     * </p>
     * 
     * <p>
     * If removal of the <code>Job</code> results in an empty group, the
     * group should be removed from the <code>JobStore</code>'s list of
     * known group names.
     * </p>
     * 
     * @param jobName
     *          The name of the <code>Job</code> to be removed.
     * @param groupName
     *          The group name of the <code>Job</code> to be removed.
     * @return <code>true</code> if a <code>Job</code> with the given name &
     *         group was found and removed from the store.
     */
public boolean removeJob(final SchedulingContext ctxt, final String jobName, final String groupName) throws JobPersistenceException {
    return ((Boolean) executeInLock(LOCK_TRIGGER_ACCESS, new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return removeJob(conn, ctxt, jobName, groupName, true) ? Boolean.TRUE : Boolean.FALSE;
        }
    })).booleanValue();
}
