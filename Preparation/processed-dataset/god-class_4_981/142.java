protected boolean doCheckin() throws JobPersistenceException {
    boolean transOwner = false;
    boolean transStateOwner = false;
    boolean recovered = false;
    Connection conn = getNonManagedTXConnection();
    try {
        // Other than the first time, always checkin first to make sure there is  
        // work to be done before we acquire the lock (since that is expensive,  
        // and is almost never necessary).  This must be done in a separate 
        // transaction to prevent a deadlock under recovery conditions. 
        List failedRecords = null;
        if (firstCheckIn == false) {
            failedRecords = clusterCheckIn(conn);
            commitConnection(conn);
        }
        if (firstCheckIn || (failedRecords.size() > 0)) {
            getLockHandler().obtainLock(conn, LOCK_STATE_ACCESS);
            transStateOwner = true;
            // Now that we own the lock, make sure we still have work to do.  
            // The first time through, we also need to make sure we update/create our state record 
            failedRecords = (firstCheckIn) ? clusterCheckIn(conn) : findFailedInstances(conn);
            if (failedRecords.size() > 0) {
                getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
                //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS); 
                transOwner = true;
                clusterRecover(conn, failedRecords);
                recovered = true;
            }
        }
        commitConnection(conn);
    } catch (JobPersistenceException e) {
        rollbackConnection(conn);
        throw e;
    } finally {
        try {
            releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
        } finally {
            try {
                releaseLock(conn, LOCK_STATE_ACCESS, transStateOwner);
            } finally {
                cleanupConnection(conn);
            }
        }
    }
    firstCheckIn = false;
    return recovered;
}
