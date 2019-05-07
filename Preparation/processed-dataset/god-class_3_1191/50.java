/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isDepended(
     *      org.exolab.castor.persist.OID, java.lang.Object)
     */
public final boolean isDepended(final OID master, final Object dependent) {
    OID oid = _tracker.getOIDForObject(dependent);
    if (oid == null) {
        return false;
    }
    OID depends = oid.getDepends();
    if (depends == null) {
        return false;
    }
    return depends.equals(master);
}
