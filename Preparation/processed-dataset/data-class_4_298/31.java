public void checkIndex(PersistentStore store) {
    readLock.lock();
    try {
        NodeAVL p = getAccessor(store);
        NodeAVL f = null;
        while (p != null) {
            f = p;
            checkNodes(store, p);
            p = p.getLeft(store);
        }
        p = f;
        while (f != null) {
            checkNodes(store, f);
            f = next(store, f);
        }
    } finally {
        readLock.unlock();
    }
}
