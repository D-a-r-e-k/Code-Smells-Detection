protected TriggerFiredBundle triggerFired(Connection conn, SchedulingContext ctxt, Trigger trigger) throws JobPersistenceException {
    JobDetail job = null;
    Calendar cal = null;
    // Make sure trigger wasn't deleted, paused, or completed... 
    try {
        // if trigger was deleted, state will be STATE_DELETED 
        String state = getDelegate().selectTriggerState(conn, trigger.getName(), trigger.getGroup());
        if (!state.equals(STATE_ACQUIRED)) {
            return null;
        }
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't select trigger state: " + e.getMessage(), e);
    }
    try {
        job = retrieveJob(conn, ctxt, trigger.getJobName(), trigger.getJobGroup());
        if (job == null) {
            return null;
        }
    } catch (JobPersistenceException jpe) {
        try {
            getLog().error("Error retrieving job, setting trigger state to ERROR.", jpe);
            getDelegate().updateTriggerState(conn, trigger.getName(), trigger.getGroup(), STATE_ERROR);
        } catch (SQLException sqle) {
            getLog().error("Unable to set trigger state to ERROR.", sqle);
        }
        throw jpe;
    }
    if (trigger.getCalendarName() != null) {
        cal = retrieveCalendar(conn, ctxt, trigger.getCalendarName());
        if (cal == null) {
            return null;
        }
    }
    try {
        getDelegate().deleteFiredTrigger(conn, trigger.getFireInstanceId());
        getDelegate().insertFiredTrigger(conn, trigger, STATE_EXECUTING, job);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't insert fired trigger: " + e.getMessage(), e);
    }
    Date prevFireTime = trigger.getPreviousFireTime();
    // call triggered - to update the trigger's next-fire-time state... 
    trigger.triggered(cal);
    String state = STATE_WAITING;
    boolean force = true;
    if (job.isStateful()) {
        state = STATE_BLOCKED;
        force = false;
        try {
            getDelegate().updateTriggerStatesForJobFromOtherState(conn, job.getName(), job.getGroup(), STATE_BLOCKED, STATE_WAITING);
            getDelegate().updateTriggerStatesForJobFromOtherState(conn, job.getName(), job.getGroup(), STATE_BLOCKED, STATE_ACQUIRED);
            getDelegate().updateTriggerStatesForJobFromOtherState(conn, job.getName(), job.getGroup(), STATE_PAUSED_BLOCKED, STATE_PAUSED);
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't update states of blocked triggers: " + e.getMessage(), e);
        }
    }
    if (trigger.getNextFireTime() == null) {
        state = STATE_COMPLETE;
        force = true;
    }
    storeTrigger(conn, ctxt, trigger, job, true, state, force, false);
    job.getJobDataMap().clearDirtyFlag();
    return new TriggerFiredBundle(job, trigger, cal, trigger.getGroup().equals(Scheduler.DEFAULT_RECOVERY_GROUP), new Date(), trigger.getPreviousFireTime(), prevFireTime, trigger.getNextFireTime());
}
