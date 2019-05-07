public boolean isEmpty(PersistentStore store) {
    readLock.lock();
    try {
        return getAccessor(store) == null;
    } finally {
        readLock.unlock();
    }
}
