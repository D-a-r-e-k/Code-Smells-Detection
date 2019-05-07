/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isDeletedByOID(
     *      org.exolab.castor.persist.OID)
     */
public final boolean isDeletedByOID(final OID oid) {
    Object o = _tracker.getObjectForOID(oid.getMolder().getLockEngine(), oid, false);
    if (o != null) {
        return _tracker.isDeleted(o);
    }
    return false;
}
