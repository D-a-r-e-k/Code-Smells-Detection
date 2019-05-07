/**
     * <p>
     * Resume (un-pause) all of the <code>{@link org.quartz.Job}s</code> in
     * the given group.
     * </p>
     * 
     * <p>
     * If any of the <code>Job</code> s had <code>Trigger</code> s that
     * missed one or more fire-times, then the <code>Trigger</code>'s
     * misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseJobGroup(SchedulingContext, String)
     */
public void resumeJobGroup(final SchedulingContext ctxt, final String groupName) throws JobPersistenceException {
    executeInLock(LOCK_TRIGGER_ACCESS, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            String[] jobNames = getJobNames(conn, ctxt, groupName);
            for (int i = 0; i < jobNames.length; i++) {
                Trigger[] triggers = getTriggersForJob(conn, ctxt, jobNames[i], groupName);
                for (int j = 0; j < triggers.length; j++) {
                    resumeTrigger(conn, ctxt, triggers[j].getName(), triggers[j].getGroup());
                }
            }
        }
    });
}
