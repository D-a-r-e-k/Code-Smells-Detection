/**
     * Return the first node equal to the indexdata object. The rowdata has the
     * same column mapping as this index.
     *
     * @param session session object
     * @param store store object
     * @param rowdata array containing index column data
     * @param fieldCount count of columns to match
     * @param compareType int
     * @return iterator
     */
public RowIterator findFirstRow(Session session, PersistentStore store, Object[] rowdata, int matchCount, int compareType, boolean reversed, boolean[] map) {
    if (compareType == OpTypes.MAX) {
        return lastRow(session, store);
    }
    NodeAVL node = findNode(session, store, rowdata, defaultColMap, matchCount, compareType, TransactionManager.ACTION_READ, reversed);
    return getIterator(session, store, node, false, reversed);
}
