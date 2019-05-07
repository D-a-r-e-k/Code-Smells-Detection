/**
     * Execute the given callback having optionally aquired the given lock.
     * This uses the non-managed transaction connection.  This version is just a 
     * handy wrapper around executeInNonManagedTXLock that doesn't require a return
     * value.
     * 
     * @param lockName The name of the lock to aquire, for example 
     * "TRIGGER_ACCESS".  If null, then no lock is aquired, but the
     * lockCallback is still executed in a non-managed transaction. 
     * 
     * @see #executeInNonManagedTXLock(String, TransactionCallback)
     */
protected void executeInNonManagedTXLock(final String lockName, final VoidTransactionCallback txCallback) throws JobPersistenceException {
    executeInNonManagedTXLock(lockName, new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            txCallback.execute(conn);
            return null;
        }
    });
}
