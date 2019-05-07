protected RecoverMisfiredJobsResult recoverMisfiredJobs(Connection conn, boolean recovering) throws JobPersistenceException, SQLException {
    // If recovering, we want to handle all of the misfired 
    // triggers right away. 
    int maxMisfiresToHandleAtATime = (recovering) ? -1 : getMaxMisfiresToHandleAtATime();
    List misfiredTriggers = new ArrayList();
    long earliestNewTime = Long.MAX_VALUE;
    // We must still look for the MISFIRED state in case triggers were left  
    // in this state when upgrading to this version that does not support it.  
    boolean hasMoreMisfiredTriggers = getDelegate().selectMisfiredTriggersInStates(conn, STATE_MISFIRED, STATE_WAITING, getMisfireTime(), maxMisfiresToHandleAtATime, misfiredTriggers);
    if (hasMoreMisfiredTriggers) {
        getLog().info("Handling the first " + misfiredTriggers.size() + " triggers that missed their scheduled fire-time.  " + "More misfired triggers remain to be processed.");
    } else if (misfiredTriggers.size() > 0) {
        getLog().info("Handling " + misfiredTriggers.size() + " trigger(s) that missed their scheduled fire-time.");
    } else {
        getLog().debug("Found 0 triggers that missed their scheduled fire-time.");
        return RecoverMisfiredJobsResult.NO_OP;
    }
    for (Iterator misfiredTriggerIter = misfiredTriggers.iterator(); misfiredTriggerIter.hasNext(); ) {
        Key triggerKey = (Key) misfiredTriggerIter.next();
        Trigger trig = retrieveTrigger(conn, triggerKey.getName(), triggerKey.getGroup());
        if (trig == null) {
            continue;
        }
        doUpdateOfMisfiredTrigger(conn, null, trig, false, STATE_WAITING, recovering);
        if (trig.getNextFireTime() != null && trig.getNextFireTime().getTime() < earliestNewTime)
            earliestNewTime = trig.getNextFireTime().getTime();
    }
    return new RecoverMisfiredJobsResult(hasMoreMisfiredTriggers, misfiredTriggers.size(), earliestNewTime);
}
