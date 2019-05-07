/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#removeTxSynchronizable(
     *      org.exolab.castor.persist.TxSynchronizable)
     */
public final void removeTxSynchronizable(final TxSynchronizable synchronizable) {
    _synchronizeList.remove(synchronizable);
}
