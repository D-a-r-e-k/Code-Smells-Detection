protected Trigger[] getTriggersForJob(Connection conn, SchedulingContext ctxt, String jobName, String groupName) throws JobPersistenceException {
    Trigger[] array = null;
    try {
        array = getDelegate().selectTriggersForJob(conn, jobName, groupName);
    } catch (Exception e) {
        throw new JobPersistenceException("Couldn't obtain triggers for job: " + e.getMessage(), e);
    }
    return array;
}
