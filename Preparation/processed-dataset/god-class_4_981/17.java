/**
     * <p>
     * Set the maximum number of misfired triggers that the misfire handling
     * thread will try to recover at one time (within one transaction).  The
     * default is 20.
     * </p>
     */
public void setMaxMisfiresToHandleAtATime(int maxToRecoverAtATime) {
    this.maxToRecoverAtATime = maxToRecoverAtATime;
}
