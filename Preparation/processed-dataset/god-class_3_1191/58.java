/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#expireCache(
     *      org.exolab.castor.persist.ClassMolder,
     *      org.exolab.castor.persist.spi.Identity)
     */
public final synchronized void expireCache(final ClassMolder molder, final Identity identity) throws PersistenceException {
    OID oid;
    LockEngine engine = molder.getLockEngine();
    if (identity == null) {
        throw new PersistenceException("Identities can't be null!");
    }
    oid = new OID(molder, identity);
    Object trackedObject = _tracker.getObjectForOID(engine, oid, false);
    if (trackedObject == null) {
        try {
            // the call to engine.expireCache may result in a  
            // recursive call to this.expireCache, therefore,  
            // an entry is added to the object list to prevent  
            // infinite loops due to bi-directional references  
            _tracker.trackObject(molder, oid, identity);
            if (engine.expireCache(this, oid, _lockTimeout)) {
                engine.releaseLock(this, oid);
            }
        } catch (LockNotGrantedException except) {
            throw except;
        } finally {
            _tracker.untrackObject(identity);
        }
    }
}
