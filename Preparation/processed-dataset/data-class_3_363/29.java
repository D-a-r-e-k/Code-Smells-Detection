/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#create(
     *      org.exolab.castor.persist.ClassMolder,
     *      java.lang.Object, org.exolab.castor.persist.OID)
     */
public final synchronized void create(final ClassMolder molder, final Object object, final OID depended) throws PersistenceException {
    // markCreate will walk the object tree starting from the specified  
    // object and mark all the object to be created.  
    markCreate(molder, object, depended);
    walkObjectsToBeCreated();
    walkObjectsWhichNeedCacheUpdate();
}
