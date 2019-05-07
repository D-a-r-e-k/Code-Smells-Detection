/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isUpdatePersistNeeded(java.lang.Object)
     */
public final boolean isUpdatePersistNeeded(final Object object) {
    return _tracker.isUpdatePersistNeeded(object);
}
