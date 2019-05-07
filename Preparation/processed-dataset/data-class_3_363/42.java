/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#iterateReadWriteObjectsInTransaction()
     */
public final Iterator iterateReadWriteObjectsInTransaction() {
    return _tracker.getReadWriteObjects().iterator();
}
