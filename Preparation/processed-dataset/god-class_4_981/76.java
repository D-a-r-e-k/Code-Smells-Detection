protected boolean replaceTrigger(Connection conn, SchedulingContext ctxt, String triggerName, String groupName, Trigger newTrigger) throws JobPersistenceException {
    try {
        // this must be called before we delete the trigger, obviously 
        JobDetail job = getDelegate().selectJobForTrigger(conn, triggerName, groupName, getClassLoadHelper());
        if (job == null) {
            return false;
        }
        if (!newTrigger.getJobName().equals(job.getName()) || !newTrigger.getJobGroup().equals(job.getGroup())) {
            throw new JobPersistenceException("New trigger is not related to the same job as the old trigger.");
        }
        boolean removedTrigger = deleteTriggerAndChildren(conn, triggerName, groupName);
        storeTrigger(conn, ctxt, newTrigger, job, false, STATE_WAITING, false, false);
        return removedTrigger;
    } catch (ClassNotFoundException e) {
        throw new JobPersistenceException("Couldn't remove trigger: " + e.getMessage(), e);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't remove trigger: " + e.getMessage(), e);
    }
}
