/**
     * Execute the given callback in a transaction. Depending on the JobStore, 
     * the surrounding transaction may be assumed to be already present 
     * (managed).  
     * 
     * <p>
     * This method just forwards to executeInLock() with a null lockName.
     * </p>
     * 
     * @see #executeInLock(String, TransactionCallback)
     */
public Object executeWithoutLock(TransactionCallback txCallback) throws JobPersistenceException {
    return executeInLock(null, txCallback);
}
