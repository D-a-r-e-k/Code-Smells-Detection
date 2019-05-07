/**
     * Returns the node after the given one
     */
NodeAVL next(Session session, PersistentStore store, NodeAVL x) {
    if (x == null) {
        return null;
    }
    readLock.lock();
    try {
        while (true) {
            x = next(store, x);
            if (x == null) {
                return x;
            }
            if (session == null) {
                return x;
            }
            Row row = x.getRow(store);
            if (session.database.txManager.canRead(session, row, TransactionManager.ACTION_READ, null)) {
                return x;
            }
        }
    } finally {
        readLock.unlock();
    }
}
