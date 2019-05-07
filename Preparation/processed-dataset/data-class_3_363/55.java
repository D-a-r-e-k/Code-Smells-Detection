/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isDeleted(java.lang.Object)
     */
public final boolean isDeleted(final Object object) {
    return _tracker.isDeleted(object);
}
