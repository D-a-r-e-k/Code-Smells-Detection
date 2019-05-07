/**
     * <p>
     * Remove (delete) the <code>{@link org.quartz.Calendar}</code> with the
     * given name.
     * </p>
     * 
     * <p>
     * If removal of the <code>Calendar</code> would result in
     * <code>Trigger</code>s pointing to non-existent calendars, then a
     * <code>JobPersistenceException</code> will be thrown.</p>
     *       *
     * @param calName The name of the <code>Calendar</code> to be removed.
     * @return <code>true</code> if a <code>Calendar</code> with the given name
     * was found and removed from the store.
     */
public boolean removeCalendar(final SchedulingContext ctxt, final String calName) throws JobPersistenceException {
    return ((Boolean) executeInLock(LOCK_TRIGGER_ACCESS, new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return removeCalendar(conn, ctxt, calName) ? Boolean.TRUE : Boolean.FALSE;
        }
    })).booleanValue();
}
