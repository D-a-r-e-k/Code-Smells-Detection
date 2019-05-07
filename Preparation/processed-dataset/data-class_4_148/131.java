/**
     * <p>
     * Inform the <code>JobStore</code> that the scheduler is now firing the
     * given <code>Trigger</code> (executing its associated <code>Job</code>),
     * that it had previously acquired (reserved).
     * </p>
     * 
     * @return null if the trigger or its job or calendar no longer exist, or
     *         if the trigger was not successfully put into the 'executing'
     *         state.
     */
public TriggerFiredBundle triggerFired(final SchedulingContext ctxt, final Trigger trigger) throws JobPersistenceException {
    return (TriggerFiredBundle) executeInNonManagedTXLock(LOCK_TRIGGER_ACCESS, new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            try {
                return triggerFired(conn, ctxt, trigger);
            } catch (JobPersistenceException jpe) {
                // If job didn't exisit, we still want to commit our work and return null. 
                if (jpe.getErrorCode() == SchedulerException.ERR_PERSISTENCE_JOB_DOES_NOT_EXIST) {
                    return null;
                } else {
                    throw jpe;
                }
            }
        }
    });
}
