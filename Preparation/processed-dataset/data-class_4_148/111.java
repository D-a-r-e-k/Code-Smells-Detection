/**
     * Determines if a Trigger for the given job should be blocked.  
     * State can only transition to STATE_PAUSED_BLOCKED/STATE_BLOCKED from 
     * STATE_PAUSED/STATE_WAITING respectively.
     * 
     * @return STATE_PAUSED_BLOCKED, STATE_BLOCKED, or the currentState. 
     */
protected String checkBlockedState(Connection conn, SchedulingContext ctxt, String jobName, String jobGroupName, String currentState) throws JobPersistenceException {
    // State can only transition to BLOCKED from PAUSED or WAITING. 
    if ((currentState.equals(STATE_WAITING) == false) && (currentState.equals(STATE_PAUSED) == false)) {
        return currentState;
    }
    try {
        List lst = getDelegate().selectFiredTriggerRecordsByJob(conn, jobName, jobGroupName);
        if (lst.size() > 0) {
            FiredTriggerRecord rec = (FiredTriggerRecord) lst.get(0);
            if (rec.isJobIsStateful()) {
                // TODO: worry about 
                // failed/recovering/volatile job 
                // states? 
                return (STATE_PAUSED.equals(currentState)) ? STATE_PAUSED_BLOCKED : STATE_BLOCKED;
            }
        }
        return currentState;
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't determine if trigger should be in a blocked state '" + jobGroupName + "." + jobName + "': " + e.getMessage(), e);
    }
}
