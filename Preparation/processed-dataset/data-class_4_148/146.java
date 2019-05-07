protected List clusterCheckIn(Connection conn) throws JobPersistenceException {
    List failedInstances = findFailedInstances(conn);
    try {
        // TODO: handle self-failed-out 
        // check in... 
        lastCheckin = System.currentTimeMillis();
        if (getDelegate().updateSchedulerState(conn, getInstanceId(), lastCheckin) == 0) {
            getDelegate().insertSchedulerState(conn, getInstanceId(), lastCheckin, getClusterCheckinInterval());
        }
    } catch (Exception e) {
        throw new JobPersistenceException("Failure updating scheduler state when checking-in: " + e.getMessage(), e);
    }
    return failedInstances;
}
