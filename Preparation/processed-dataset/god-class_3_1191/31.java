private void walkObjectsWhichNeedCacheUpdate() {
    // after we create the objects, some cache may invalid because the  
    // relation are cached on both side. So, we updateCache if it is  
    // marked to be update from the markCreate state  
    Collection objectsMarkedForUpdate = _tracker.getObjectsWithUpdateCacheNeededState();
    Iterator it = objectsMarkedForUpdate.iterator();
    while (it.hasNext()) {
        Object toCacheUpdate = it.next();
        if (_tracker.isCreated(toCacheUpdate)) {
            OID toCacheUpdateOID = _tracker.getOIDForObject(toCacheUpdate);
            LockEngine toCacheUpdateLocker = _tracker.getMolderForObject(toCacheUpdate).getLockEngine();
            toCacheUpdateLocker.updateCache(this, toCacheUpdateOID, toCacheUpdate);
            _tracker.unmarkUpdateCacheNeeded(toCacheUpdate);
        }
    }
}
