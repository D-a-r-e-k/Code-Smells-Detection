protected String[] getTriggerNames(Connection conn, SchedulingContext ctxt, String groupName) throws JobPersistenceException {
    String[] trigNames = null;
    try {
        trigNames = getDelegate().selectTriggersInGroup(conn, groupName);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't obtain trigger names: " + e.getMessage(), e);
    }
    return trigNames;
}
