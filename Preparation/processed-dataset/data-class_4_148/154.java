/**
     * Execute the given callback having aquired the given lock.  
     * Depending on the JobStore, the surrounding transaction may be 
     * assumed to be already present (managed).  This version is just a 
     * handy wrapper around executeInLock that doesn't require a return
     * value.
     * 
     * @param lockName The name of the lock to aquire, for example 
     * "TRIGGER_ACCESS".  If null, then no lock is aquired, but the
     * lockCallback is still executed in a transaction. 
     * 
     * @see #executeInLock(String, TransactionCallback)
     */
protected void executeInLock(final String lockName, final VoidTransactionCallback txCallback) throws JobPersistenceException {
    executeInLock(lockName, new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            txCallback.execute(conn);
            return null;
        }
    });
}
