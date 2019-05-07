/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isOpen()
     */
public final boolean isOpen() {
    return ((_status == Status.STATUS_ACTIVE) || (_status == Status.STATUS_MARKED_ROLLBACK));
}
