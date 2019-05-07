public int sizeUnique(PersistentStore store) {
    readLock.lock();
    try {
        return store.elementCountUnique(this);
    } finally {
        readLock.unlock();
    }
}
