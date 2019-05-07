/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#commit()
     */
public final synchronized void commit() throws TransactionAbortedException {
    // Never commit transaction that has been marked for rollback  
    if (_status == Status.STATUS_MARKED_ROLLBACK) {
        throw new TransactionAbortedException("persist.markedRollback");
    }
    if (_status != Status.STATUS_PREPARED) {
        throw new IllegalStateException(Messages.message("persist.missingPrepare"));
    }
    try {
        _status = Status.STATUS_COMMITTING;
        // Go through all the connections opened in this transaction,  
        // commit and close them one by one.  
        commitConnections();
    } catch (Exception except) {
        // Any error that happens, we're going to rollback the transaction.  
        _status = Status.STATUS_MARKED_ROLLBACK;
        throw new TransactionAbortedException(Messages.format("persist.nested", except), except);
    }
    // Assuming all went well in the connection department,  
    // no deadlocks, etc. clean all the transaction locks with  
    // regards to the persistence engine.  
    Collection readWriteObjects = _tracker.getReadWriteObjects();
    Iterator it = readWriteObjects.iterator();
    while (it.hasNext()) {
        Object toCommit = it.next();
        ClassMolder molder = _tracker.getMolderForObject(toCommit);
        LockEngine engine = molder.getLockEngine();
        OID oid = _tracker.getOIDForObject(toCommit);
        if (_tracker.isDeleted(toCommit)) {
            // Object has been deleted inside transaction,  
            // engine must forget about it.  
            engine.forgetObject(this, oid);
        } else {
            // Object has been created/accessed inside the  
            // transaction, release its lock.  
            if (_tracker.isUpdateCacheNeeded(toCommit)) {
                engine.updateCache(this, oid, toCommit);
            }
            engine.releaseLock(this, oid);
        }
        // Call our release callback on all processed objects.  
        if (_callback != null) {
            _callback.releasing(toCommit, true);
        } else if (molder.getCallback() != null) {
            molder.getCallback().releasing(toCommit, true);
        }
    }
    // Call txcommited() before objects are removed to allow  
    // TxSynchronizable to iterate through the objects.  
    txcommitted();
    // Forget about all the objects in this transaction,  
    // and mark it as completed.  
    _tracker.clear();
    _status = Status.STATUS_COMMITTED;
}
