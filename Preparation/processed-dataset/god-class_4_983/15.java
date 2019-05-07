/**
     * <p>
     * Returns a handle to the Scheduler produced by this factory.
     * </p>
     *
     * <p>
     * If one of the <code>initialize</code> methods has not be previously
     * called, then the default (no-arg) <code>initialize()</code> method
     * will be called by this method.
     * </p>
     */
public Scheduler getScheduler() throws SchedulerException {
    if (cfg == null) {
        initialize();
    }
    SchedulerRepository schedRep = SchedulerRepository.getInstance();
    Scheduler sched = schedRep.lookup(getSchedulerName());
    if (sched != null) {
        if (sched.isShutdown()) {
            schedRep.remove(getSchedulerName());
        } else {
            return sched;
        }
    }
    sched = instantiate();
    return sched;
}
