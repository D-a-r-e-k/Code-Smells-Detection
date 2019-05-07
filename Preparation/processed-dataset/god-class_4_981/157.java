/**
     * Execute the given callback having optionally aquired the given lock.
     * This uses the non-managed transaction connection.
     * 
     * @param lockName The name of the lock to aquire, for example 
     * "TRIGGER_ACCESS".  If null, then no lock is aquired, but the
     * lockCallback is still executed in a non-managed transaction. 
     */
protected Object executeInNonManagedTXLock(String lockName, TransactionCallback txCallback) throws JobPersistenceException {
    boolean transOwner = false;
    Connection conn = null;
    try {
        if (lockName != null) {
            // If we aren't using db locks, then delay getting DB connection  
            // until after acquiring the lock since it isn't needed. 
            if (getLockHandler().requiresConnection()) {
                conn = getNonManagedTXConnection();
            }
            transOwner = getLockHandler().obtainLock(conn, lockName);
        }
        if (conn == null) {
            conn = getNonManagedTXConnection();
        }
        Object result = txCallback.execute(conn);
        commitConnection(conn);
        Long sigTime = clearAndGetSignalSchedulingChangeOnTxCompletion();
        if (sigTime != null && sigTime >= 0) {
            signalSchedulingChangeImmediately(sigTime);
        }
        return result;
    } catch (JobPersistenceException e) {
        rollbackConnection(conn);
        throw e;
    } catch (RuntimeException e) {
        rollbackConnection(conn);
        throw new JobPersistenceException("Unexpected runtime exception: " + e.getMessage(), e);
    } finally {
        try {
            releaseLock(conn, lockName, transOwner);
        } finally {
            cleanupConnection(conn);
        }
    }
}
