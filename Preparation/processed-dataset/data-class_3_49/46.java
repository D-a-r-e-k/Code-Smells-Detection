/**
     * The cache has reached its cacpacity and an item needs to be removed.
     * (typically according to an algorithm such as LRU or FIFO).
     *
     * @return The key of whichever item was removed.
     */
protected abstract Object removeItem();
