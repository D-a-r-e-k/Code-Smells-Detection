/**
     * <p>
     * Returns a handle to the default Scheduler, creating it if it does not
     * yet exist.
     * </p>
     *
     * @see #initialize()
     */
public static Scheduler getDefaultScheduler() throws SchedulerException {
    StdSchedulerFactory fact = new StdSchedulerFactory();
    return fact.getScheduler();
}
