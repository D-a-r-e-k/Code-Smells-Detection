/**
     * Returns true if the object given is locked.
     * 
     * @param cls Class instance of the object to be investigated.
     * @param identity Identity of the object to be investigated. 
     * @param lockEngine Current LcokEngine instance
     * @return True if the object in question is locked.
     */
public final boolean isLocked(final Class cls, final Identity identity, final LockEngine lockEngine) {
    OID oid = new OID(lockEngine.getClassMolder(cls), identity);
    return lockEngine.isLocked(cls, oid);
}
