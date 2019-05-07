public int getNodeCount(Session session, PersistentStore store) {
    int count = 0;
    readLock.lock();
    try {
        RowIterator it = firstRow(session, store);
        while (it.hasNext()) {
            it.getNextRow();
            count++;
        }
        return count;
    } finally {
        readLock.unlock();
    }
}
