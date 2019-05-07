/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#close()
     */
public final synchronized void close() throws TransactionAbortedException {
    if (_status != Status.STATUS_ACTIVE && _status != Status.STATUS_MARKED_ROLLBACK) {
        throw new IllegalStateException(Messages.message("persist.missingEnd"));
    }
    try {
        // Go through all the connections opened in this transaction,  
        // close them one by one.  
        closeConnections();
    } catch (Exception except) {
        // Any error that happens, we're going to rollback the transaction.  
        _status = Status.STATUS_MARKED_ROLLBACK;
        throw new TransactionAbortedException(Messages.format("persist.nested", except), except);
    }
}
