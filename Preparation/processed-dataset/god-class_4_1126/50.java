/**
     * Returns the row for the last node of the index
     *
     * @return last row
     */
public RowIterator lastRow(Session session, PersistentStore store) {
    readLock.lock();
    try {
        NodeAVL x = getAccessor(store);
        NodeAVL l = x;
        while (l != null) {
            x = l;
            l = x.getRight(store);
        }
        while (session != null && x != null) {
            Row row = x.getRow(store);
            if (session.database.txManager.canRead(session, row, TransactionManager.ACTION_READ, null)) {
                break;
            }
            x = last(store, x);
        }
        return getIterator(null, store, x, false, true);
    } finally {
        readLock.unlock();
    }
}
