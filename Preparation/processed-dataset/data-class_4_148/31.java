/**
     * Whether or not the query and update to acquire a Trigger for firing
     * should be performed after obtaining an explicit DB lock.  This is the
     * behavior prior to Quartz 1.6.3, but is considered unnecessary for most
     * databases, and therefore a superfluous performance hit.     
     */
public void setAcquireTriggersWithinLock(boolean acquireTriggersWithinLock) {
    this.acquireTriggersWithinLock = acquireTriggersWithinLock;
}
