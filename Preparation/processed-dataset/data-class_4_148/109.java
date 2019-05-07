/**
     * <p>
     * Pause the <code>{@link org.quartz.Job}</code> with the given name - by
     * pausing all of its current <code>Trigger</code>s.
     * </p>
     * 
     * @see #resumeJob(SchedulingContext, String, String)
     */
public void pauseJob(final SchedulingContext ctxt, final String jobName, final String groupName) throws JobPersistenceException {
    executeInLock(LOCK_TRIGGER_ACCESS, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            Trigger[] triggers = getTriggersForJob(conn, ctxt, jobName, groupName);
            for (int j = 0; j < triggers.length; j++) {
                pauseTrigger(conn, ctxt, triggers[j].getName(), triggers[j].getGroup());
            }
        }
    });
}
