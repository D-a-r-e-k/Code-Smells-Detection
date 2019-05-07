/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#rollback()
     */
public final synchronized void rollback() {
    if (_status != Status.STATUS_ACTIVE && _status != Status.STATUS_PREPARED && _status != Status.STATUS_MARKED_ROLLBACK) {
        throw new IllegalStateException(Messages.message("persist.noTransaction"));
    }
    // Go through all the connections opened in this transaction,  
    // rollback and close them one by one.  
    rollbackConnections();
    // un-delete object first  
    _tracker.unmarkAllDeleted();
    // Clean the transaction locks with regards to the database engine.  
    Collection readWriteObjects = _tracker.getReadWriteObjects();
    OID oid = null;
    try {
        Iterator it = readWriteObjects.iterator();
        // First revert all objects  
        while (it.hasNext()) {
            Object object = it.next();
            LockEngine engine = _tracker.getMolderForObject(object).getLockEngine();
            oid = _tracker.getOIDForObject(object);
            if (!_tracker.isCreating(object)) {
                engine.revertObject(this, oid, object);
            }
        }
        // then forget object or release lock on them  
        it = readWriteObjects.iterator();
        while (it.hasNext()) {
            Object object = it.next();
            ClassMolder molder = _tracker.getMolderForObject(object);
            LockEngine engine = molder.getLockEngine();
            oid = _tracker.getOIDForObject(object);
            if (!_tracker.isCreating(object)) {
                if (_tracker.isCreated(object)) {
                    // Object has been created in this transaction,  
                    // it no longer exists, forget about it in the engine.  
                    engine.forgetObject(this, oid);
                } else {
                    // Object has been queried (possibly) deleted in this  
                    // transaction and release the lock.  
                    engine.releaseLock(this, oid);
                }
            }
            if (_callback != null) {
                _callback.releasing(object, false);
            } else if (molder.getCallback() != null) {
                molder.getCallback().releasing(object, false);
            }
        }
    } catch (Exception except) {
        // Don't thow exceptions during a rollback. Just report them.  
        LOG.error("Caught exception at rollback of object with OID " + oid, except);
    }
    // Forget about all the objects in this transaction,  
    // and mark it as completed.  
    _tracker.clear();
    txrolledback();
    _status = Status.STATUS_ROLLEDBACK;
}
