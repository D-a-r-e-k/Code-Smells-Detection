protected void storeCalendar(Connection conn, SchedulingContext ctxt, String calName, Calendar calendar, boolean replaceExisting, boolean updateTriggers) throws ObjectAlreadyExistsException, JobPersistenceException {
    try {
        boolean existingCal = calendarExists(conn, calName);
        if (existingCal && !replaceExisting) {
            throw new ObjectAlreadyExistsException("Calendar with name '" + calName + "' already exists.");
        }
        if (existingCal) {
            if (getDelegate().updateCalendar(conn, calName, calendar) < 1) {
                throw new JobPersistenceException("Couldn't store calendar.  Update failed.");
            }
            if (updateTriggers) {
                Trigger[] trigs = getDelegate().selectTriggersForCalendar(conn, calName);
                for (int i = 0; i < trigs.length; i++) {
                    trigs[i].updateWithNewCalendar(calendar, getMisfireThreshold());
                    storeTrigger(conn, ctxt, trigs[i], null, true, STATE_WAITING, false, false);
                }
            }
        } else {
            if (getDelegate().insertCalendar(conn, calName, calendar) < 1) {
                throw new JobPersistenceException("Couldn't store calendar.  Insert failed.");
            }
        }
        if (isClustered == false) {
            calendarCache.put(calName, calendar);
        }
    } catch (IOException e) {
        throw new JobPersistenceException("Couldn't store calendar because the BLOB couldn't be serialized: " + e.getMessage(), e);
    } catch (ClassNotFoundException e) {
        throw new JobPersistenceException("Couldn't store calendar: " + e.getMessage(), e);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't store calendar: " + e.getMessage(), e);
    }
}
