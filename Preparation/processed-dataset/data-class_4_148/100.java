protected String[] getJobGroupNames(Connection conn, SchedulingContext ctxt) throws JobPersistenceException {
    String[] groupNames = null;
    try {
        groupNames = getDelegate().selectJobGroups(conn);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't obtain job groups: " + e.getMessage(), e);
    }
    return groupNames;
}
