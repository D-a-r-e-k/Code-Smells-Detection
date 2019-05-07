/**
     * <p>
     * Get the maximum number of misfired triggers that the misfire handling
     * thread will try to recover at one time (within one transaction).  The
     * default is 20.
     * </p>
     */
public int getMaxMisfiresToHandleAtATime() {
    return maxToRecoverAtATime;
}
