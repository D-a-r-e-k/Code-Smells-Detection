/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#addTxSynchronizable(
     *      org.exolab.castor.persist.TxSynchronizable)
     */
public final void addTxSynchronizable(final TxSynchronizable synchronizable) {
    _synchronizeList.add(synchronizable);
}
