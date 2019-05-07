/*
     * private List findTriggersToBeBlocked(Connection conn, SchedulingContext
     * ctxt, String groupName) throws JobPersistenceException {
     * 
     * try { List blockList = new LinkedList();
     * 
     * List affectingJobs =
     * getDelegate().selectStatefulJobsOfTriggerGroup(conn, groupName);
     * 
     * Iterator itr = affectingJobs.iterator(); while(itr.hasNext()) { Key
     * jobKey = (Key) itr.next();
     * 
     * List lst = getDelegate().selectFiredTriggerRecordsByJob(conn,
     * jobKey.getName(), jobKey.getGroup());
     * 
     * This logic is BROKEN...
     * 
     * if(lst.size() > 0) { FiredTriggerRecord rec =
     * (FiredTriggerRecord)lst.get(0); if(rec.isJobIsStateful()) // TODO: worry
     * about failed/recovering/volatile job states? blockList.add(
     * rec.getTriggerKey() ); } }
     * 
     * 
     * return blockList; } catch (SQLException e) { throw new
     * JobPersistenceException ("Couldn't determine states of resumed triggers
     * in group '" + groupName + "': " + e.getMessage(), e); } }
     */
/**
     * <p>
     * Resume (un-pause) the <code>{@link org.quartz.Trigger}</code> with the
     * given name.
     * </p>
     * 
     * <p>
     * If the <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseTrigger(SchedulingContext, String, String)
     */
public void resumeTrigger(final SchedulingContext ctxt, final String triggerName, final String groupName) throws JobPersistenceException {
    executeInLock(LOCK_TRIGGER_ACCESS, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            resumeTrigger(conn, ctxt, triggerName, groupName);
        }
    });
}
