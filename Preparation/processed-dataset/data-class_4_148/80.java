/**
     * <p>
     * Get the current state of the identified <code>{@link Trigger}</code>.
     * </p>
     * 
     * @see Trigger#STATE_NORMAL
     * @see Trigger#STATE_PAUSED
     * @see Trigger#STATE_COMPLETE
     * @see Trigger#STATE_ERROR
     * @see Trigger#STATE_NONE
     */
public int getTriggerState(final SchedulingContext ctxt, final String triggerName, final String groupName) throws JobPersistenceException {
    return ((Integer) executeWithoutLock(// no locks necessary for read... 
    new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return new Integer(getTriggerState(conn, ctxt, triggerName, groupName));
        }
    })).intValue();
}
