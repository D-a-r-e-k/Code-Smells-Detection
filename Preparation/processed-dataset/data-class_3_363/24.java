/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#load(
     *      org.exolab.castor.persist.spi.Identity, org.castor.persist.ProposedEntity,
     *      org.exolab.castor.mapping.AccessMode, org.exolab.castor.persist.QueryResults)
     */
public final synchronized Object load(final Identity identity, final ProposedEntity proposedObject, final AccessMode suggestedAccessMode, final QueryResults results) throws PersistenceException {
    Object objectInTx;
    OID oid;
    AccessMode accessMode;
    ClassMolder molder = proposedObject.getActualClassMolder();
    LockEngine engine = molder.getLockEngine();
    if (identity == null) {
        throw new PersistenceException("Identities can't be null!");
    }
    // Test that the object to be loaded (which we will fill in) is of an  
    // appropriate type for our molder.  
    if (proposedObject.getEntity() != null && !molder.getJavaClass(_db.getClassLoader()).isAssignableFrom(proposedObject.getProposedEntityClass())) {
        throw new PersistenceException(Messages.format("persist.typeMismatch", molder.getName(), proposedObject.getProposedEntityClass()));
    }
    oid = new OID(molder, identity);
    accessMode = molder.getAccessMode(suggestedAccessMode);
    if (accessMode == AccessMode.ReadOnly) {
        objectInTx = _tracker.getObjectForOID(engine, oid, true);
    } else {
        objectInTx = _tracker.getObjectForOID(engine, oid, false);
    }
    if (objectInTx != null) {
        // Object exists in this transaction.  
        // If the object has been loaded, but the instance sugguested to  
        // be loaded into is not the same as the loaded instance,  
        // error is reported.  
        // TODO [WG]: could read && propsedObject != objectInTransaction  
        if (proposedObject.getEntity() != null && proposedObject.getEntity() != objectInTx) {
            throw new PersistenceException(Messages.format("persist.multipleLoad", molder.getName(), identity));
        }
        // If the object has been loaded in this transaction from a  
        // different engine this is an error. If the object has been  
        // deleted in this transaction, it cannot be re-loaded. If the  
        // object has been created in this transaction, it cannot be  
        // re-loaded but no error is reported.  
        if (engine != _tracker.getMolderForObject(objectInTx).getLockEngine()) {
            throw new PersistenceException(Messages.format("persist.multipleLoad", molder.getName(), identity));
        }
        // Objects marked deleted in the transaction therefore we  
        // throw a ObjectNotFoundException to signal that object isn't  
        // available any more.  
        if (_tracker.isDeleted(objectInTx)) {
            throw new ObjectNotFoundException(Messages.format("persist.objectNotFound", molder.getName(), identity));
        }
        // ssa, multi classloader feature (note - this code appears to be  
        // duplicated, yet different, in both cases. Why?)  
        // ssa, FIXME : Are the two following statements equivalent ? (bug  
        // 998)  
        // if ( ! molder.getJavaClass().isAssignableFrom(  
        // entry.object.getClass() ) )  
        // if ( ! molder.getJavaClass( _db.getClassLoader()  
        // ).isAssignableFrom( entry.object.getClass() ) )  
        if (!molder.getJavaClass(_db.getClassLoader()).isAssignableFrom(objectInTx.getClass())) {
            throw new PersistenceException(Messages.format("persist.typeMismatch", molder.getName(), objectInTx.getClass()));
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
        proposedObject.setProposedEntityClass(objectInTx.getClass());
        proposedObject.setActualEntityClass(objectInTx.getClass());
        proposedObject.setEntity(objectInTx);
        return objectInTx;
    }
    // Load (or reload, in case the object is stored in a acache) the object  
    // through the persistence engine with the requested lock. This might report  
    // failure (object no longer exists), hold until a suitable lock is granted  
    // (or fail to grant), or report error with the persistence engine.  
    try {
        // Check whether an instance was given to the load method.  
        if (proposedObject.getEntity() != null) {
            objectInTx = proposedObject.getEntity();
        } else if (_instanceFactory != null) {
            objectInTx = _instanceFactory.newInstance(molder.getName(), _db.getClassLoader());
        } else {
            objectInTx = molder.newInstance(_db.getClassLoader());
        }
        molder.setIdentity(this, objectInTx, identity);
        proposedObject.setProposedEntityClass(objectInTx.getClass());
        proposedObject.setActualEntityClass(objectInTx.getClass());
        proposedObject.setEntity(objectInTx);
        trackObject(molder, oid, proposedObject.getEntity());
        engine.load(this, oid, proposedObject, suggestedAccessMode, _lockTimeout, results, molder);
    } catch (ClassCastException except) {
        _tracker.untrackObject(proposedObject.getEntity());
        throw except;
    } catch (ObjectNotFoundException except) {
        _tracker.untrackObject(proposedObject.getEntity());
        throw except;
    } catch (ConnectionFailedException except) {
        _tracker.untrackObject(proposedObject.getEntity());
        throw except;
    } catch (LockNotGrantedException except) {
        _tracker.untrackObject(proposedObject.getEntity());
        throw except;
    } catch (ClassNotPersistenceCapableException except) {
        _tracker.untrackObject(proposedObject.getEntity());
        throw new PersistenceException(Messages.format("persist.nested", except));
    } catch (InstantiationException e) {
        _tracker.untrackObject(proposedObject.getEntity());
        throw new PersistenceException(e.getMessage(), e);
    } catch (IllegalAccessException e) {
        _tracker.untrackObject(proposedObject.getEntity());
        throw new PersistenceException(e.getMessage(), e);
    } catch (ClassNotFoundException e) {
        _tracker.untrackObject(proposedObject.getEntity());
        throw new PersistenceException(e.getMessage(), e);
    }
    objectInTx = proposedObject.getEntity();
    // Need to copy the contents of this object from the cached  
    // copy and deal with it based on the transaction semantics.  
    // If the mode is read-only we release the lock and forget about  
    // it in the contents of this transaction. Otherwise we record  
    // the object in this transaction.  
    try {
        if (_callback != null) {
            _callback.using(objectInTx, _db);
            _callback.loaded(objectInTx, accessMode);
        } else if (molder.getCallback() != null) {
            molder.getCallback().using(objectInTx, _db);
            molder.getCallback().loaded(objectInTx, accessMode);
        }
    } catch (Exception except) {
        release(objectInTx);
        throw new PersistenceException(Messages.format("persist.nested", except));
    }
    if (accessMode == AccessMode.ReadOnly) {
        // Mark it read-only.  
        _tracker.markReadOnly(objectInTx);
        // Release the lock on this object.  
        engine.releaseLock(this, oid);
    }
    return objectInTx;
}
