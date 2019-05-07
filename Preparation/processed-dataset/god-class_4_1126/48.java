/**
     * Returns the row for the first node of the index
     *
     * @return Iterator for first row
     */
public RowIterator firstRow(Session session, PersistentStore store) {
    int tempDepth = 0;
    readLock.lock();
    try {
        NodeAVL x = getAccessor(store);
        NodeAVL l = x;
        while (l != null) {
            x = l;
            l = x.getLeft(store);
            tempDepth++;
        }
        while (session != null && x != null) {
            Row row = x.getRow(store);
            if (session.database.txManager.canRead(session, row, TransactionManager.ACTION_READ, null)) {
                break;
            }
            x = next(store, x);
        }
        return getIterator(session, store, x, false, false);
    } finally {
        depth = tempDepth;
        readLock.unlock();
    }
}
