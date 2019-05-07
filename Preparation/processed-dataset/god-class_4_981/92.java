protected int getNumberOfTriggers(Connection conn, SchedulingContext ctxt) throws JobPersistenceException {
    try {
        return getDelegate().selectNumTriggers(conn);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't obtain number of triggers: " + e.getMessage(), e);
    }
}
