private void prepareForCreate() throws PersistenceException {
    Collection allObjects = _tracker.getReadWriteObjects();
    Iterator it = allObjects.iterator();
    while (it.hasNext()) {
        Object toPrepare = it.next();
        boolean isCreating = _tracker.isCreating(toPrepare);
        boolean isDeleted = _tracker.isDeleted(toPrepare);
        boolean needsPersist = _tracker.isUpdatePersistNeeded(toPrepare);
        boolean needsCache = _tracker.isUpdateCacheNeeded(toPrepare);
        ClassMolder molder = _tracker.getMolderForObject(toPrepare);
        LockEngine engine = molder.getLockEngine();
        OID oid = _tracker.getOIDForObject(toPrepare);
        if (!isDeleted && !isCreating) {
            if (needsPersist) {
                engine.store(this, oid, toPrepare);
            }
            if (needsCache) {
                engine.softLock(this, oid, _lockTimeout);
            }
        }
        // do the callback  
        if (!isDeleted && _callback != null) {
            try {
                _callback.storing(toPrepare, needsCache);
            } catch (Exception except) {
                throw new TransactionAbortedException(Messages.format("persist.nested", except), except);
            }
        } else if (!isDeleted && molder.getCallback() != null) {
            try {
                molder.getCallback().storing(toPrepare, needsCache);
            } catch (Exception except) {
                throw new TransactionAbortedException(Messages.format("persist.nested", except), except);
            }
        }
    }
}
