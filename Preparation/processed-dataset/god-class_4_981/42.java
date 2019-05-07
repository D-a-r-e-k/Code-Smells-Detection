/**
     * Set whether to check to see if there are Triggers that have misfired
     * before actually acquiring the lock to recover them.  This should be 
     * set to false if the majority of the time, there are are misfired
     * Triggers.
     */
public void setDoubleCheckLockMisfireHandler(boolean doubleCheckLockMisfireHandler) {
    this.doubleCheckLockMisfireHandler = doubleCheckLockMisfireHandler;
}
