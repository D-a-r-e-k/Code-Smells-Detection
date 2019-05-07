/**
     * <p>
     * Will recover any failed or misfired jobs and clean up the data store as
     * appropriate.
     * </p>
     * 
     * @throws JobPersistenceException
     *           if jobs could not be recovered
     */
protected void recoverJobs(Connection conn) throws JobPersistenceException {
    try {
        // update inconsistent job states 
        int rows = getDelegate().updateTriggerStatesFromOtherStates(conn, STATE_WAITING, STATE_ACQUIRED, STATE_BLOCKED);
        rows += getDelegate().updateTriggerStatesFromOtherStates(conn, STATE_PAUSED, STATE_PAUSED_BLOCKED, STATE_PAUSED_BLOCKED);
        getLog().info("Freed " + rows + " triggers from 'acquired' / 'blocked' state.");
        // clean up misfired jobs 
        recoverMisfiredJobs(conn, true);
        // recover jobs marked for recovery that were not fully executed 
        Trigger[] recoveringJobTriggers = getDelegate().selectTriggersForRecoveringJobs(conn);
        getLog().info("Recovering " + recoveringJobTriggers.length + " jobs that were in-progress at the time of the last shut-down.");
        for (int i = 0; i < recoveringJobTriggers.length; ++i) {
            if (jobExists(conn, recoveringJobTriggers[i].getJobName(), recoveringJobTriggers[i].getJobGroup())) {
                recoveringJobTriggers[i].computeFirstFireTime(null);
                storeTrigger(conn, null, recoveringJobTriggers[i], null, false, STATE_WAITING, false, true);
            }
        }
        getLog().info("Recovery complete.");
        // remove lingering 'complete' triggers... 
        Key[] ct = getDelegate().selectTriggersInState(conn, STATE_COMPLETE);
        for (int i = 0; ct != null && i < ct.length; i++) {
            removeTrigger(conn, null, ct[i].getName(), ct[i].getGroup());
        }
        getLog().info("Removed " + (ct != null ? ct.length : 0) + " 'complete' triggers.");
        // clean up any fired trigger entries 
        int n = getDelegate().deleteFiredTriggers(conn);
        getLog().info("Removed " + n + " stale fired job entries.");
    } catch (JobPersistenceException e) {
        throw e;
    } catch (Exception e) {
        throw new JobPersistenceException("Couldn't recover jobs: " + e.getMessage(), e);
    }
}
