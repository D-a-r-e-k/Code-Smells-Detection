protected boolean updateMisfiredTrigger(Connection conn, SchedulingContext ctxt, String triggerName, String groupName, String newStateIfNotComplete, boolean forceState) throws JobPersistenceException {
    try {
        Trigger trig = retrieveTrigger(conn, triggerName, groupName);
        long misfireTime = System.currentTimeMillis();
        if (getMisfireThreshold() > 0) {
            misfireTime -= getMisfireThreshold();
        }
        if (trig.getNextFireTime().getTime() > misfireTime) {
            return false;
        }
        doUpdateOfMisfiredTrigger(conn, ctxt, trig, forceState, newStateIfNotComplete, false);
        schedSignaler.notifySchedulerListenersFinalized(trig);
        return true;
    } catch (Exception e) {
        throw new JobPersistenceException("Couldn't update misfired trigger '" + groupName + "." + triggerName + "': " + e.getMessage(), e);
    }
}
