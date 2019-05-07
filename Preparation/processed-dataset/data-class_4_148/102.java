protected String[] getTriggerGroupNames(Connection conn, SchedulingContext ctxt) throws JobPersistenceException {
    String[] groupNames = null;
    try {
        groupNames = getDelegate().selectTriggerGroups(conn);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't obtain trigger groups: " + e.getMessage(), e);
    }
    return groupNames;
}
