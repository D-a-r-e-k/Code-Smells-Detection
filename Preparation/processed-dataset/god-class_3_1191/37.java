/**
     * Releases the lock granted on the object. The object is removed from this
     * transaction and will not participate in transaction commit/abort. Any
     * changes done to the object are lost.
     * 
     * @param object The object to release the lock.
     * @throws PersistenceException The object was not queried or created in this
     *         transaction.An error occured talking to the persistence engine.
     */
private synchronized void release(final Object object) throws PersistenceException {
    if (object == null) {
        throw new PersistenceException("Object to release lock is null!");
    }
    // Get the entry for this object, if it does not exist  
    // the object has never been persisted in this transaction  
    if (!_tracker.isTracking(object)) {
        throw new ObjectNotPersistentException(Messages.format("persist.objectNotPersistent", object.getClass().getName().getClass()));
    }
    ClassMolder molder = _tracker.getMolderForObject(object);
    LockEngine engine = molder.getLockEngine();
    OID oid = _tracker.getOIDForObject(object);
    if (_tracker.isDeleted(object)) {
        throw new ObjectDeletedException(Messages.format("persist.objectDeleted", object.getClass().getName(), oid.getIdentity()));
    }
    // Release the lock, forget about the object in this transaction  
    engine.releaseLock(this, oid);
    _tracker.untrackObject(object);
    if (_callback != null) {
        _callback.releasing(object, false);
    } else if (molder != null && molder.getCallback() != null) {
        molder.getCallback().releasing(object, false);
    }
    if (engine == null) {
        throw new PersistenceException("Release: " + "Missing engine during release call; fundamental tracking error.");
    }
    if (molder == null) {
        throw new PersistenceException("Release: " + "Missing molder during release call; fundamental tracking error.");
    }
    if (oid == null) {
        throw new PersistenceException("Release: " + "Missing OID during release call; fundamental tracking error.");
    }
}
