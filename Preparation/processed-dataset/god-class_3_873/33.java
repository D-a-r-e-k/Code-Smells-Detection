/**
     * Get ref to table; the reference and the cells it
     * accesses will be at least as fresh as from last
     * use of barrierLock
     **/
protected final Entry[] getTableForReading() {
    synchronized (barrierLock) {
        return table;
    }
}
