/**
     * <p>
     * Pause all of the <code>{@link org.quartz.Job}s</code> in the given
     * group - by pausing all of their <code>Trigger</code>s.
     * </p>
     * 
     * @see #resumeJobGroup(SchedulingContext, String)
     */
public void pauseJobGroup(final SchedulingContext ctxt, final String groupName) throws JobPersistenceException {
    executeInLock(LOCK_TRIGGER_ACCESS, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            String[] jobNames = getJobNames(conn, ctxt, groupName);
            for (int i = 0; i < jobNames.length; i++) {
                Trigger[] triggers = getTriggersForJob(conn, ctxt, jobNames[i], groupName);
                for (int j = 0; j < triggers.length; j++) {
                    pauseTrigger(conn, ctxt, triggers[j].getName(), triggers[j].getGroup());
                }
            }
        }
    });
}
