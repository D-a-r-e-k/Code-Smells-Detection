/**
     * Get a list of all scheduler instances in the cluster that may have failed.
     * This includes this scheduler if it is checking in for the first time.
     */
protected List findFailedInstances(Connection conn) throws JobPersistenceException {
    try {
        List failedInstances = new LinkedList();
        boolean foundThisScheduler = false;
        long timeNow = System.currentTimeMillis();
        List states = getDelegate().selectSchedulerStateRecords(conn, null);
        for (Iterator itr = states.iterator(); itr.hasNext(); ) {
            SchedulerStateRecord rec = (SchedulerStateRecord) itr.next();
            // find own record... 
            if (rec.getSchedulerInstanceId().equals(getInstanceId())) {
                foundThisScheduler = true;
                if (firstCheckIn) {
                    failedInstances.add(rec);
                }
            } else {
                // find failed instances... 
                if (calcFailedIfAfter(rec) < timeNow) {
                    failedInstances.add(rec);
                }
            }
        }
        // The first time through, also check for orphaned fired triggers. 
        if (firstCheckIn) {
            failedInstances.addAll(findOrphanedFailedInstances(conn, states));
        }
        // If not the first time but we didn't find our own instance, then 
        // Someone must have done recovery for us. 
        if ((foundThisScheduler == false) && (firstCheckIn == false)) {
            // TODO: revisit when handle self-failed-out implied (see TODO in clusterCheckIn() below) 
            getLog().warn("This scheduler instance (" + getInstanceId() + ") is still " + "active but was recovered by another instance in the cluster.  " + "This may cause inconsistent behavior.");
        }
        return failedInstances;
    } catch (Exception e) {
        lastCheckin = System.currentTimeMillis();
        throw new JobPersistenceException("Failure identifying failed instances when checking-in: " + e.getMessage(), e);
    }
}
