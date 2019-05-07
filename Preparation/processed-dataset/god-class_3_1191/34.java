/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#delete(java.lang.Object)
     */
public final synchronized void delete(final Object object) throws PersistenceException {
    if (object == null) {
        throw new PersistenceException("Object to be deleted is null!");
    }
    // Get the entry for this object, if it does not exist  
    // the object has never been persisted in this transaction  
    if (!_tracker.isTracking(object)) {
        throw new ObjectNotPersistentException(Messages.format("persist.objectNotPersistent", object.getClass().getName()));
    }
    ClassMolder molder = _tracker.getMolderForObject(object);
    LockEngine engine = molder.getLockEngine();
    OID oid = _tracker.getOIDForObject(object);
    // Cannot delete same object twice  
    if (_tracker.isDeleted(object)) {
        throw new ObjectDeletedException(Messages.format("persist.objectDeleted", object.getClass().getName(), oid.getIdentity()));
    }
    try {
        if (_callback != null) {
            _callback.removing(object);
        } else if (molder.getCallback() != null) {
            molder.getCallback().removing(object);
        }
    } catch (Exception except) {
        throw new PersistenceException(Messages.format("persist.nested", except));
    }
    // Must acquire a write lock on the object in order to delete it,  
    // prevents object form being deleted while someone else is  
    // looking at it.  
    try {
        _tracker.markDeleted(object);
        engine.softLock(this, oid, _lockTimeout);
        // Mark object as deleted. This will prevent it from being viewed  
        // in this transaction and will handle it properly at commit time.  
        // The write lock will prevent it from being viewed in another  
        // transaction.  
        engine.markDelete(this, oid, object, _lockTimeout);
        try {
            if (_callback != null) {
                _callback.removed(object);
            } else if (molder.getCallback() != null) {
                molder.getCallback().removed(object);
            }
        } catch (Exception except) {
            throw new PersistenceException(Messages.format("persist.nested", except));
        }
    } catch (ObjectDeletedException except) {
        // Object has been deleted outside this transaction,  
        // forget about it  
        _tracker.untrackObject(object);
    }
}
