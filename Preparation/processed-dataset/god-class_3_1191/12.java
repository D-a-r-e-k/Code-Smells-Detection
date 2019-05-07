/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#setLockTimeout(int)
     */
public final void setLockTimeout(final int timeout) {
    _lockTimeout = (timeout >= 0 ? timeout : 0);
}
