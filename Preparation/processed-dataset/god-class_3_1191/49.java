/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isRecorded(java.lang.Object)
     */
public final boolean isRecorded(final Object object) {
    return _tracker.isTracking(object);
}
