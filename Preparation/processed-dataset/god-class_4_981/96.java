protected String[] getJobNames(Connection conn, SchedulingContext ctxt, String groupName) throws JobPersistenceException {
    String[] jobNames = null;
    try {
        jobNames = getDelegate().selectJobsInGroup(conn, groupName);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't obtain job names: " + e.getMessage(), e);
    }
    return jobNames;
}
