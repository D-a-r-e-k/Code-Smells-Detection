/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#markModified(
     *      java.lang.Object, boolean, boolean)
     */
public final synchronized void markModified(final Object object, final boolean updatePersist, final boolean updateCache) {
    if (updatePersist) {
        _tracker.markUpdatePersistNeeded(object);
    }
    if (updateCache) {
        _tracker.markUpdateCacheNeeded(object);
    }
}
