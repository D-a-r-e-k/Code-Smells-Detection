/**
     * Get ref to groups.
     * The reference and the cells it
     * accesses will be at least as fresh as from last
     * use of barrierLock
     **/
protected final Map getGroupsForReading() {
    synchronized (barrierLock) {
        return groups;
    }
}
