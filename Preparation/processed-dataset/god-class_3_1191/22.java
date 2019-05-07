/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext
     *      #fetch(org.exolab.castor.persist.ClassMolder,
     *             org.exolab.castor.persist.spi.Identity,
     *             org.exolab.castor.mapping.AccessMode)
     */
public final synchronized Object fetch(final ClassMolder molder, final Identity identity, final AccessMode suggestedAccessMode) throws PersistenceException {
    Object objectInTx;
    OID oid;
    AccessMode accessMode;
    if (identity == null) {
        throw new PersistenceException("Identities can't be null!");
    }
    LockEngine engine = molder.getLockEngine();
    oid = new OID(molder, identity);
    accessMode = molder.getAccessMode(suggestedAccessMode);
    if (accessMode == AccessMode.ReadOnly) {
        objectInTx = _tracker.getObjectForOID(engine, oid, true);
    } else {
        objectInTx = _tracker.getObjectForOID(engine, oid, false);
    }
    if (objectInTx != null) {
        // Object exists in this transaction.  
        // If the object has been loaded in this transaction from a  
        // different engine this is an error. If the object has been  
        // deleted in this transaction, it cannot be re-loaded. If the  
        // object has been created in this transaction, it cannot be  
        // re-loaded but no error is reported.  
        if (engine != _tracker.getMolderForObject(objectInTx).getLockEngine()) {
            throw new PersistenceException(Messages.format("persist.multipleLoad", molder.getName(), identity));
        }
        // Objects marked deleted in the transaction return null.  
        if (_tracker.isDeleted(objectInTx)) {
            return null;
        }
        // ssa, multi classloader feature (note - this code appears to be  
        // duplicated, yet different, in both cases. Why?)  
        // ssa, FIXME : Are the two following statements equivalent ? (bug  
        // 998)  
        // if ( ! molder.getJavaClass().isAssignableFrom(  
        // entry.object.getClass() ) )  
        // if ( ! molder.getJavaClass( _db.getClassLoader()  
        // ).isAssignableFrom( entry.object.getClass() ) )  
        if (!molder.isAssignableFrom(objectInTx.getClass())) {
            throw new PersistenceException(Messages.format("persist.typeMismatch", molder.getName(), identity));
        }
        // If the object has been created in this transaction, don't bother  
        // testing access mode.  
        if (_tracker.isCreated(objectInTx)) {
            return objectInTx;
        }
        if ((accessMode == AccessMode.Exclusive || accessMode == AccessMode.DbLocked) && !_tracker.getOIDForObject(objectInTx).isDbLock()) {
            // If we are in exclusive mode and object has not been  
            // loaded in exclusive mode before, then we have a  
            // problem. We cannot return an object that is not  
            // synchronized with the database, but we cannot  
            // synchronize a live object.  
            throw new PersistenceException(Messages.format("persist.lockConflict", molder.getName(), identity));
        }
        return objectInTx;
    }
    return null;
}
