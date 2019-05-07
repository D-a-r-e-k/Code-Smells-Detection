/**
     * Returns the node count.
     */
public int size(Session session, PersistentStore store) {
    readLock.lock();
    try {
        return store.elementCount(session);
    } finally {
        readLock.unlock();
    }
}
