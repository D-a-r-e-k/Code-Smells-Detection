private void prepareForDelete() throws PersistenceException {
    Collection objectsToDelete = _tracker.getObjectsWithDeletedStateSortedByHighestMolderPriority();
    Iterator it = objectsToDelete.iterator();
    while (it.hasNext()) {
        Object object = it.next();
        LockEngine engine = _tracker.getMolderForObject(object).getLockEngine();
        OID oid = _tracker.getOIDForObject(object);
        engine.delete(this, oid);
    }
}
