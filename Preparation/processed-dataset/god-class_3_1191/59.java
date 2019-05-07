/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isCached(
     *      org.exolab.castor.persist.ClassMolder,
     *      java.lang.Class, org.exolab.castor.persist.spi.Identity)
     */
public final boolean isCached(final ClassMolder molder, final Class cls, final Identity identity) throws PersistenceException {
    if (identity == null) {
        throw new PersistenceException("Identities can't be null!");
    }
    OID oid = new OID(molder, identity);
    return molder.getLockEngine().isCached(cls, oid);
}
