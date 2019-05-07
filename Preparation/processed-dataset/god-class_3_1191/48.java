/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isPersistent(java.lang.Object)
     */
public final boolean isPersistent(final Object object) {
    return (_tracker.isTracking(object) && !_tracker.isDeleted(object));
}
