/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isCreated(java.lang.Object)
     */
public final boolean isCreated(final Object object) {
    return _tracker.isCreated(object);
}
