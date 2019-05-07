protected String[] getCalendarNames(Connection conn, SchedulingContext ctxt) throws JobPersistenceException {
    try {
        return getDelegate().selectCalendars(conn);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't obtain trigger groups: " + e.getMessage(), e);
    }
}
