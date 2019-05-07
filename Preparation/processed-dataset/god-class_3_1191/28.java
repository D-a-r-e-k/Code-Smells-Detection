/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#markCreate(
     *      org.exolab.castor.persist.ClassMolder,
     *      java.lang.Object, org.exolab.castor.persist.OID)
     */
public final synchronized void markCreate(final ClassMolder molder, final Object object, final OID rootObjectOID) throws PersistenceException {
    if (object == null) {
        throw new PersistenceException("Attempted to mark a null object as created.");
    }
    LockEngine engine = molder.getLockEngine();
    // Make sure the object has not beed persisted in this transaction.  
    Identity identity = molder.getIdentity(this, object);
    // if autoStore is specified, we relieve user life a little bit here  
    // so that if an object create automatically and user create it  
    // again, it won't receive exception  
    if (_autoStore && _tracker.isTracking(object)) {
        return;
    }
    if (_tracker.isDeleted(object)) {
        OID deletedoid = _tracker.getOIDForObject(object);
        throw new PersistenceException(Messages.format("persist.objectAlreadyPersistent", object.getClass().getName(), (deletedoid != null) ? deletedoid.getIdentity() : null));
    }
    // Create the object. This can only happen once for each object in  
    // all transactions running on the same engine, so after creation  
    // add a new entry for this object and use this object as the view  
    // Note that the oid which is created is for a dependent object;  
    // this is not a change to the rootObjectOID, and therefore doesn't get  
    // trackOIDChange()d.  
    OID oid = new OID(molder, rootObjectOID, identity);
    // You shouldn't be able to modify an object marked read-only in this  
    // transaction.  
    Object trackedObject = _tracker.getObjectForOID(engine, oid, false);
    if (identity != null && trackedObject != null) {
        if (trackedObject != object) {
            throw new DuplicateIdentityException("Object being tracked with the OID created for a dependent " + "object does not match the object to be marked for " + "creation. Fundamental Tracking Error.");
        } else if (_tracker.isDeleted(object)) {
            // Undelete it.  
            _tracker.unmarkDeleted(object);
        }
    }
    try {
        _tracker.trackObject(molder, oid, object);
        _tracker.markCreating(object);
        if (_callback != null) {
            _callback.creating(object, _db);
        } else if (molder.getCallback() != null) {
            molder.getCallback().creating(object, _db);
        }
        engine.markCreate(this, oid, object);
    } catch (LockNotGrantedException lneg) {
        // yip: do we need LockNotGrantedException, or should we  
        // removed them?  
        _tracker.untrackObject(object);
        // Note: This used to throw a very strange empty-string  
        // DuplicateIdentityException, which is of course bollocks.  
        throw lneg;
    } catch (PersistenceException pe) {
        _tracker.untrackObject(object);
        throw pe;
    } catch (Exception e) {
        _tracker.untrackObject(object);
        throw new PersistenceException(Messages.format("persist.nested", e), e);
    }
}
