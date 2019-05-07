/**
     * Return the first node equal to the rowdata object.
     * The rowdata has the same column mapping as this table.
     *
     * @param session session object
     * @param store store object
     * @param rowdata array containing table row data
     * @return iterator
     */
public RowIterator findFirstRow(Session session, PersistentStore store, Object[] rowdata) {
    NodeAVL node = findNode(session, store, rowdata, colIndex, colIndex.length, OpTypes.EQUAL, TransactionManager.ACTION_READ, false);
    return getIterator(session, store, node, false, false);
}
