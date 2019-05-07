protected boolean removeCalendar(Connection conn, SchedulingContext ctxt, String calName) throws JobPersistenceException {
    try {
        if (getDelegate().calendarIsReferenced(conn, calName)) {
            throw new JobPersistenceException("Calender cannot be removed if it referenced by a trigger!");
        }
        if (isClustered == false) {
            calendarCache.remove(calName);
        }
        return (getDelegate().deleteCalendar(conn, calName) > 0);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't remove calendar: " + e.getMessage(), e);
    }
}
