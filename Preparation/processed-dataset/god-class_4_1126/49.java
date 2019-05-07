public RowIterator firstRow(PersistentStore store) {
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
        return getIterator(null, store, x, false, false);
    } finally {
        depth = tempDepth;
        readLock.unlock();
    }
}
