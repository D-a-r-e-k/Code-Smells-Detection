/** 
     * @see org.quartz.spi.JobStore#replaceTrigger(org.quartz.core.SchedulingContext, java.lang.String, java.lang.String, org.quartz.Trigger)
     */
public boolean replaceTrigger(final SchedulingContext ctxt, final String triggerName, final String groupName, final Trigger newTrigger) throws JobPersistenceException {
    return ((Boolean) executeInLock(LOCK_TRIGGER_ACCESS, new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return replaceTrigger(conn, ctxt, triggerName, groupName, newTrigger) ? Boolean.TRUE : Boolean.FALSE;
        }
    })).booleanValue();
}
