/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#writeLock(java.lang.Object, int)
     */
public final synchronized void writeLock(final Object object, final int timeout) throws PersistenceException {
    if (object == null) {
        throw new PersistenceException("Object to acquire lock is null!");
    }
    // Get the entry for this object, if it does not exist  
    // the object has never been persisted in this transaction  
    if (!_tracker.isTracking(object)) {
        throw new ObjectNotPersistentException(Messages.format("persist.objectNotPersistent", object.getClass().getName()));
    }
    LockEngine engine = _tracker.getMolderForObject(object).getLockEngine();
    OID oid = _tracker.getOIDForObject(object);
    if (_tracker.isDeleted(object)) {
        throw new ObjectDeletedException(Messages.format("persist.objectDeleted", object.getClass(), oid.getIdentity()));
    }
    try {
        engine.writeLock(this, oid, timeout);
    } catch (ObjectDeletedException except) {
        // Object has been deleted outside this transaction,  
        // forget about it  
        _tracker.untrackObject(object);
        throw new ObjectNotPersistentException(Messages.format("persist.objectNotPersistent", object.getClass().getName()));
    } catch (LockNotGrantedException except) {
        // Can't get lock, but may still keep running  
        throw except;
    }
}
