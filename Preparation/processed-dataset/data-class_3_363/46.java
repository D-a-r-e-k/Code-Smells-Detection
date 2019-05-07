/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isUpdateCacheNeeded(java.lang.Object)
     */
public final boolean isUpdateCacheNeeded(final Object object) {
    return _tracker.isUpdateCacheNeeded(object);
}
