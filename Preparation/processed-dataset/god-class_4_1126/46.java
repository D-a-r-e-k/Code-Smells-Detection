/**
     * Return the first node equal to the rowdata object. The rowdata has the
     * column mapping privided in rowColMap.
     *
     * @param session session object
     * @param store store object
     * @param rowdata array containing table row data
     * @param rowColMap int[]
     * @return iterator
     */
public RowIterator findFirstRow(Session session, PersistentStore store, Object[] rowdata, int[] rowColMap) {
    NodeAVL node = findNode(session, store, rowdata, rowColMap, rowColMap.length, OpTypes.EQUAL, TransactionManager.ACTION_READ, false);
    return getIterator(session, store, node, false, false);
}
