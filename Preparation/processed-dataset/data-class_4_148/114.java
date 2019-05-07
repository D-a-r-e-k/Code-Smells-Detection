/**
     * <p>
     * Resume (un-pause) the <code>{@link org.quartz.Job}</code> with the
     * given name.
     * </p>
     * 
     * <p>
     * If any of the <code>Job</code>'s<code>Trigger</code> s missed one
     * or more fire-times, then the <code>Trigger</code>'s misfire
     * instruction will be applied.
     * </p>
     * 
     * @see #pauseJob(SchedulingContext, String, String)
     */
public void resumeJob(final SchedulingContext ctxt, final String jobName, final String groupName) throws JobPersistenceException {
    executeInLock(LOCK_TRIGGER_ACCESS, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            Trigger[] triggers = getTriggersForJob(conn, ctxt, jobName, groupName);
            for (int j = 0; j < triggers.length; j++) {
                resumeTrigger(conn, ctxt, triggers[j].getName(), triggers[j].getGroup());
            }
        }
    });
}
