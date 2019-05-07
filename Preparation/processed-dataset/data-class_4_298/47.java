/**
     * Finds the first node where the data is not null.
     *
     * @return iterator
     */
public RowIterator findFirstRowNotNull(Session session, PersistentStore store) {
    NodeAVL node = findNode(session, store, nullData, this.defaultColMap, 1, OpTypes.NOT, TransactionManager.ACTION_READ, false);
    return getIterator(session, store, node, false, false);
}
