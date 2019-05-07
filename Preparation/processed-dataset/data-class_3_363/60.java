/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isReadOnly(java.lang.Object)
     */
public final boolean isReadOnly(final Object object) {
    return _tracker.isReadOnly(object);
}
