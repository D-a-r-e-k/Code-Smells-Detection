/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#prepare()
     */
public final synchronized boolean prepare() throws TransactionAbortedException {
    ArrayList<Object> todo = new ArrayList<Object>();
    ArrayList<Object> done = new ArrayList<Object>();
    if (_status == Status.STATUS_MARKED_ROLLBACK) {
        throw new TransactionAbortedException("persist.markedRollback");
    }
    if (_status != Status.STATUS_ACTIVE) {
        throw new IllegalStateException(Messages.message("persist.noTransaction"));
    }
    try {
        // No objects in this transaction -- this is a read only transaction  
        // Put it into the try block to close connections  
        if (_tracker.readWriteSize() == 0) {
            _status = Status.STATUS_PREPARED;
            return false;
        }
        Collection readWriteObjects = _tracker.getReadWriteObjects();
        while (readWriteObjects.size() != done.size()) {
            todo.clear();
            Iterator rwIterator = readWriteObjects.iterator();
            while (rwIterator.hasNext()) {
                Object object = rwIterator.next();
                if (!done.contains(object)) {
                    todo.add(object);
                }
            }
            Iterator<Object> todoIterator = todo.iterator();
            while (todoIterator.hasNext()) {
                Object object = todoIterator.next();
                // Anything not marked 'deleted' or 'creating' is ready to  
                // consider for store.  
                if ((!_tracker.isDeleted(object)) && (!_tracker.isCreating(object))) {
                    LockEngine engine = _tracker.getMolderForObject(object).getLockEngine();
                    //_tracker.getMolderForObject(object);  
                    OID oid = _tracker.getOIDForObject(object);
                    OID newoid = engine.preStore(this, oid, object, _lockTimeout);
                    if (newoid != null) {
                        _tracker.trackOIDChange(object, engine, oid, newoid);
                        _tracker.markUpdateCacheNeeded(object);
                    }
                }
                done.add(object);
            }
        }
        // preStore will actually walk all existing object and it might  
        // marked  
        // some object to be created (and to be removed).  
        walkObjectsToBeCreated();
        // Now mark anything ready for create to create them.  
        prepareForCreate();
        _status = Status.STATUS_PREPARING;
        prepareForDelete();
        _status = Status.STATUS_PREPARED;
        return true;
    } catch (Exception except) {
        _status = Status.STATUS_MARKED_ROLLBACK;
        if (except instanceof TransactionAbortedException) {
            throw (TransactionAbortedException) except;
        }
        // Any error is reported as transaction aborted  
        throw new TransactionAbortedException(Messages.format("persist.nested", except), except);
    }
}
