protected JobDetail retrieveJob(Connection conn, SchedulingContext ctxt, String jobName, String groupName) throws JobPersistenceException {
    try {
        JobDetail job = getDelegate().selectJobDetail(conn, jobName, groupName, getClassLoadHelper());
        if (job != null) {
            String[] listeners = getDelegate().selectJobListeners(conn, jobName, groupName);
            for (int i = 0; i < listeners.length; ++i) {
                job.addJobListener(listeners[i]);
            }
        }
        return job;
    } catch (ClassNotFoundException e) {
        throw new JobPersistenceException("Couldn't retrieve job because a required class was not found: " + e.getMessage(), e, SchedulerException.ERR_PERSISTENCE_JOB_DOES_NOT_EXIST);
    } catch (IOException e) {
        throw new JobPersistenceException("Couldn't retrieve job because the BLOB couldn't be deserialized: " + e.getMessage(), e, SchedulerException.ERR_PERSISTENCE_JOB_DOES_NOT_EXIST);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't retrieve job: " + e.getMessage(), e);
    }
}
