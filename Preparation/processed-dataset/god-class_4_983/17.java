/**
     * <p>
     * Returns a handle to the Scheduler with the given name, if it exists (if
     * it has already been instantiated).
     * </p>
     */
public Scheduler getScheduler(String schedName) throws SchedulerException {
    return SchedulerRepository.getInstance().lookup(schedName);
}
