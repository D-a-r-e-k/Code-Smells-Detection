protected boolean removeTrigger(Connection conn, SchedulingContext ctxt, String triggerName, String groupName) throws JobPersistenceException {
    boolean removedTrigger = false;
    try {
        // this must be called before we delete the trigger, obviously 
        JobDetail job = getDelegate().selectJobForTrigger(conn, triggerName, groupName, getClassLoadHelper());
        removedTrigger = deleteTriggerAndChildren(conn, triggerName, groupName);
        if (null != job && !job.isDurable()) {
            int numTriggers = getDelegate().selectNumTriggersForJob(conn, job.getName(), job.getGroup());
            if (numTriggers == 0) {
                // Don't call removeJob() because we don't want to check for 
                // triggers again. 
                deleteJobAndChildren(conn, ctxt, job.getName(), job.getGroup());
            }
        }
    } catch (ClassNotFoundException e) {
        throw new JobPersistenceException("Couldn't remove trigger: " + e.getMessage(), e);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't remove trigger: " + e.getMessage(), e);
    }
    return removedTrigger;
}
