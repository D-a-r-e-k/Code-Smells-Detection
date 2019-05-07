/**
     * After performing create()/update() calls, walk all objects which are to
     * be created in association with that call; lastly, perform the necessary
     * cache updates to those objects if necessary.
     * 
     * @throws PersistenceException
     */
private synchronized void walkObjectsToBeCreated() throws PersistenceException {
    // After the marking is done, we will actually create the object.  
    // However, because some objects contains foreign key are key  
    // generated, such object should be created after some other.  
    // We iterate all object and creating object according the priority.  
    Collection createableObjects = _tracker.getObjectsWithCreatingStateSortedByLowestMolderPriority();
    Iterator creatableIterator = createableObjects.iterator();
    while (creatableIterator.hasNext()) {
        // Must perform creation after object is recorded in transaction  
        // to prevent circular references.  
        Object toBeCreated = creatableIterator.next();
        OID toBeCreatedOID = _tracker.getOIDForObject(toBeCreated);
        ClassMolder toBeCreatedMolder = _tracker.getMolderForObject(toBeCreated);
        LockEngine toBeCreatedLockEngine = toBeCreatedMolder.getLockEngine();
        try {
            // Allow users to create inside the callback.  
            // We do this by rechecking that the object is still marked  
            // creatable;  
            // this will tell us if another process got to it first.  
            if (_tracker.isCreating(toBeCreated)) {
                if (_callback != null) {
                    _callback.creating(toBeCreated, _db);
                } else if (toBeCreatedMolder.getCallback() != null) {
                    toBeCreatedMolder.getCallback().creating(toBeCreated, _db);
                }
                OID oid = toBeCreatedLockEngine.create(this, toBeCreatedOID, toBeCreated);
                if (oid.getIdentity() == null) {
                    throw new IllegalStateException("oid.getIdentity() is null after create!");
                }
                // rehash the object entry, in case of oid changed  
                _tracker.trackOIDChange(toBeCreated, toBeCreatedLockEngine, toBeCreatedOID, oid);
                _tracker.markCreated(toBeCreated);
                if (_callback != null) {
                    _callback.using(toBeCreated, _db);
                    _callback.created(toBeCreated);
                } else if (toBeCreatedMolder.getCallback() != null) {
                    toBeCreatedMolder.getCallback().using(toBeCreated, _db);
                    toBeCreatedMolder.getCallback().created(toBeCreated);
                }
            }
        } catch (Exception except) {
            if (_callback != null) {
                _callback.releasing(toBeCreated, false);
            } else if (toBeCreatedMolder.getCallback() != null) {
                toBeCreatedMolder.getCallback().releasing(toBeCreated, false);
            }
            _tracker.untrackObject(toBeCreated);
            if (except instanceof DuplicateIdentityException) {
                throw (DuplicateIdentityException) except;
            } else if (except instanceof PersistenceException) {
                throw (PersistenceException) except;
            } else {
                throw new PersistenceException(Messages.format("persist.nested", except), except);
            }
        }
    }
}
