/**
     * <p>
     * Store the given <code>{@link org.quartz.JobDetail}</code> and <code>{@link org.quartz.Trigger}</code>.
     * </p>
     * 
     * @param newJob
     *          The <code>JobDetail</code> to be stored.
     * @param newTrigger
     *          The <code>Trigger</code> to be stored.
     * @throws ObjectAlreadyExistsException
     *           if a <code>Job</code> with the same name/group already
     *           exists.
     */
public void storeJobAndTrigger(final SchedulingContext ctxt, final JobDetail newJob, final Trigger newTrigger) throws ObjectAlreadyExistsException, JobPersistenceException {
    executeInLock((isLockOnInsert()) ? LOCK_TRIGGER_ACCESS : null, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            if (newJob.isVolatile() && !newTrigger.isVolatile()) {
                JobPersistenceException jpe = new JobPersistenceException("Cannot associate non-volatile trigger with a volatile job!");
                jpe.setErrorCode(SchedulerException.ERR_CLIENT_ERROR);
                throw jpe;
            }
            storeJob(conn, ctxt, newJob, false);
            storeTrigger(conn, ctxt, newTrigger, newJob, false, Constants.STATE_WAITING, false, false);
        }
    });
}
