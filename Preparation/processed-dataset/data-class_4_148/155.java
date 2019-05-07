/**
     * Execute the given callback having aquired the given lock.  
     * Depending on the JobStore, the surrounding transaction may be 
     * assumed to be already present (managed).
     * 
     * @param lockName The name of the lock to aquire, for example 
     * "TRIGGER_ACCESS".  If null, then no lock is aquired, but the
     * lockCallback is still executed in a transaction. 
     */
protected abstract Object executeInLock(String lockName, TransactionCallback txCallback) throws JobPersistenceException;
