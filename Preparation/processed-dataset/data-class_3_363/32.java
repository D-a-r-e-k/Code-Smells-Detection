/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#markUpdate(
     *      org.exolab.castor.persist.ClassMolder,
     *      java.lang.Object, org.exolab.castor.persist.OID)
     */
public final boolean markUpdate(final ClassMolder molder, final Object object, final OID depended) throws PersistenceException {
    if (object == null) {
        throw new NullPointerException();
    }
    LockEngine engine = molder.getLockEngine();
    Identity identity = molder.getActualIdentity(this, object);
    if (molder.isDefaultIdentity(identity)) {
        identity = null;
    }
    OID oid = new OID(molder, depended, identity);
    // Check the object is in the transaction.  
    Object foundInTransaction = _tracker.getObjectForOID(engine, oid, false);
    if (_autoStore && foundInTransaction != null && foundInTransaction == object) {
        return false;
    }
    if (foundInTransaction != null) {
        if (_tracker.isDeleted(foundInTransaction)) {
            throw new ObjectDeletedException(Messages.format("persist.objectDeleted", object.getClass(), identity));
        }
        throw new DuplicateIdentityException("update object which is already in the transaction");
    }
    try {
        _tracker.trackObject(molder, oid, object);
        if (engine.update(this, oid, object, null, 0)) {
            _tracker.markCreating(object);
        }
    } catch (DuplicateIdentityException lneg) {
        _tracker.untrackObject(object);
        throw lneg;
    } catch (PersistenceException pe) {
        _tracker.untrackObject(object);
        throw pe;
    } catch (Exception e) {
        _tracker.untrackObject(object);
        throw new PersistenceException(Messages.format("persist.nested", e), e);
    }
    if (!_tracker.isCreating(object)) {
        try {
            if (_callback != null) {
                _callback.using(object, _db);
                _callback.updated(object);
            } else if (molder.getCallback() != null) {
                molder.getCallback().using(object, _db);
                molder.getCallback().updated(object);
            }
        } catch (Exception except) {
            release(object);
            if (except instanceof PersistenceException) {
                throw (PersistenceException) except;
            }
            throw new PersistenceException(except.getMessage(), except);
        }
        return false;
    }
    return true;
}
