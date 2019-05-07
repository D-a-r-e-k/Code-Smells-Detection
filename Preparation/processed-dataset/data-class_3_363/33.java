/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#update(
     *      org.exolab.castor.persist.ClassMolder,
     *      java.lang.Object, org.exolab.castor.persist.OID)
     */
public final synchronized void update(final ClassMolder molder, final Object object, final OID depended) throws PersistenceException {
    markUpdate(molder, object, depended);
    walkObjectsToBeCreated();
    walkObjectsWhichNeedCacheUpdate();
}
