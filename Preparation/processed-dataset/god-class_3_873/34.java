/**
     * Force a memory synchronization that will cause
     * all readers to see table. Call only when already
     * holding main synch lock.
     **/
protected final void recordModification(Object x) {
    synchronized (barrierLock) {
        lastWrite = x;
    }
}
