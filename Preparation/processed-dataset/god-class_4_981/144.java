/**
     * Create dummy <code>SchedulerStateRecord</code> objects for fired triggers
     * that have no scheduler state record.  Checkin timestamp and interval are
     * left as zero on these dummy <code>SchedulerStateRecord</code> objects.
     * 
     * @param schedulerStateRecords List of all current <code>SchedulerStateRecords</code>
     */
private List findOrphanedFailedInstances(Connection conn, List schedulerStateRecords) throws SQLException, NoSuchDelegateException {
    List orphanedInstances = new ArrayList();
    Set allFiredTriggerInstanceNames = getDelegate().selectFiredTriggerInstanceNames(conn);
    if (allFiredTriggerInstanceNames.isEmpty() == false) {
        for (Iterator schedulerStateIter = schedulerStateRecords.iterator(); schedulerStateIter.hasNext(); ) {
            SchedulerStateRecord rec = (SchedulerStateRecord) schedulerStateIter.next();
            allFiredTriggerInstanceNames.remove(rec.getSchedulerInstanceId());
        }
        for (Iterator orphanIter = allFiredTriggerInstanceNames.iterator(); orphanIter.hasNext(); ) {
            SchedulerStateRecord orphanedInstance = new SchedulerStateRecord();
            orphanedInstance.setSchedulerInstanceId((String) orphanIter.next());
            orphanedInstances.add(orphanedInstance);
            getLog().warn("Found orphaned fired triggers for instance: " + orphanedInstance.getSchedulerInstanceId());
        }
    }
    return orphanedInstances;
}
