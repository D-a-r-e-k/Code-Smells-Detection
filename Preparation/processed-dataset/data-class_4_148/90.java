protected int getNumberOfJobs(Connection conn, SchedulingContext ctxt) throws JobPersistenceException {
    try {
        return getDelegate().selectNumJobs(conn);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't obtain number of jobs: " + e.getMessage(), e);
    }
}
