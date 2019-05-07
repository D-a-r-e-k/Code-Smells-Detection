/**
     * <p>
     * Store the given <code>{@link org.quartz.Calendar}</code>.
     * </p>
     * 
     * @param calName
     *          The name of the calendar.
     * @param calendar
     *          The <code>Calendar</code> to be stored.
     * @param replaceExisting
     *          If <code>true</code>, any <code>Calendar</code> existing
     *          in the <code>JobStore</code> with the same name & group
     *          should be over-written.
     * @throws ObjectAlreadyExistsException
     *           if a <code>Calendar</code> with the same name already
     *           exists, and replaceExisting is set to false.
     */
public void storeCalendar(final SchedulingContext ctxt, final String calName, final Calendar calendar, final boolean replaceExisting, final boolean updateTriggers) throws ObjectAlreadyExistsException, JobPersistenceException {
    executeInLock((isLockOnInsert() || updateTriggers) ? LOCK_TRIGGER_ACCESS : null, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            storeCalendar(conn, ctxt, calName, calendar, replaceExisting, updateTriggers);
        }
    });
}
